package rs.raf.rafcloudbackend.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.raf.rafcloudbackend.dto.CreateMachineRequest;
import rs.raf.rafcloudbackend.dto.MachineResponse;
import rs.raf.rafcloudbackend.model.Machine;
import rs.raf.rafcloudbackend.model.MachineState;
import rs.raf.rafcloudbackend.model.User;
import rs.raf.rafcloudbackend.repository.MachineRepository;
import rs.raf.rafcloudbackend.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.messaging.simp.SimpMessagingTemplate;


@Service
public class MachineService {

  private final MachineRepository machineRepository;
  private final UserRepository userRepository;
  private final MachineAsyncService machineAsyncService;

  public MachineService(MachineRepository machineRepository,
                        UserRepository userRepository,
                        MachineAsyncService machineAsyncService) {
    this.machineRepository = machineRepository;
    this.userRepository = userRepository;
    this.machineAsyncService = machineAsyncService;
  }





  public MachineResponse create(CreateMachineRequest req, String authEmail) {
    if (authEmail == null || authEmail.isBlank()) throw new RuntimeException("Unauthorized");

    User creator = userRepository.findByEmail(authEmail)
      .orElseThrow(() -> new RuntimeException("Unauthorized"));

    Machine m = new Machine();
    m.setName(req.getName());
    m.setActive(true);
    m.setState(MachineState.STOPPED);
    m.setCreatedAt(LocalDateTime.now());
    m.setCreatedBy(creator);

    return toResponse(machineRepository.save(m));
  }

  // Search only ACTIVE machines; admin sees all, user only own
  public List<MachineResponse> search(
    String name,
    Set<MachineState> states,
    LocalDate createdFrom,
    LocalDate createdTo,
    String authEmail,
    Collection<?> authPerms
  ) {
    boolean admin = isAdmin(authPerms);

    Specification<Machine> spec = (root, query, cb) -> cb.isTrue(root.get("active"));

    if (!admin) {
      if (authEmail == null || authEmail.isBlank()) throw new RuntimeException("Unauthorized");
      spec = spec.and((root, query, cb) -> cb.equal(root.join("createdBy").get("email"), authEmail));
    }

    if (name != null && !name.isBlank()) {
      String like = "%" + name.toLowerCase() + "%";
      spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), like));
    }

    if (states != null && !states.isEmpty()) {
      spec = spec.and((root, query, cb) -> root.get("state").in(states));
    }

    if (createdFrom != null) {
      LocalDateTime from = createdFrom.atStartOfDay();
      spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), from));
    }

    if (createdTo != null) {
      LocalDateTime toExclusive = createdTo.plusDays(1).atStartOfDay();
      spec = spec.and((root, query, cb) -> cb.lessThan(root.get("createdAt"), toExclusive));
    }

    return machineRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt"))
      .stream().map(this::toResponse).collect(Collectors.toList());
  }

  public void destroy(Long machineId, String authEmail, Collection<?> authPerms) {
    Machine m = machineRepository.findById(machineId)
      .orElseThrow(() -> new RuntimeException("Machine not found"));

    if (!m.isActive()) throw new RuntimeException("Machine not found");

    boolean admin = isAdmin(authPerms);

    if (!admin) {
      if (authEmail == null || authEmail.isBlank()) throw new RuntimeException("Unauthorized");
      if (m.getCreatedBy() == null || !authEmail.equalsIgnoreCase(m.getCreatedBy().getEmail())) {
        throw new RuntimeException("Forbidden");
      }
    }

    if (m.getState() != MachineState.STOPPED) {
      throw new RuntimeException("Machine must be STOPPED to destroy");
    }

    m.setActive(false);
    machineRepository.save(m);
  }

  private boolean isAdmin(Collection<?> perms) {
    if (perms == null) return false;
    return perms.stream().anyMatch(p -> "READ_USER".equalsIgnoreCase(String.valueOf(p)));
  }

  private MachineResponse toResponse(Machine m) {
    String createdByEmail = (m.getCreatedBy() == null) ? null : m.getCreatedBy().getEmail();
    return new MachineResponse(m.getId(), m.getName(), m.isActive(), m.getState(), m.getCreatedAt(), createdByEmail);
  }


  private Machine getAndAuthorize(Long id, String authEmail, Collection<?> perms) {
    Machine m = machineRepository.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    if (!m.isActive()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    if (m.getState() == MachineState.STARTING ||
      m.getState() == MachineState.STOPPING ||
      m.getState() == MachineState.RESTARTING) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Machine busy");
    }

    boolean admin = isAdmin(perms);
    if (!admin && !m.getCreatedBy().getEmail().equalsIgnoreCase(authEmail)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    return m;
  }


  public MachineResponse start(Long id, String authEmail, Collection<?> perms) {
    Machine m = getAndAuthorize(id, authEmail, perms);

    if (m.getState() != MachineState.STOPPED) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }

    m.setState(MachineState.STARTING);
    machineRepository.save(m);

    // ✅ POZIV DRUGOG BEAN-A
    machineAsyncService.startAsync(m.getId());

    return toResponse(m);
  }



  public MachineResponse stop(Long id, String authEmail, Collection<?> perms) {
    Machine m = getAndAuthorize(id, authEmail, perms);

    if (m.getState() != MachineState.RUNNING) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "Machine not RUNNING"
      );
    }

    // 1️⃣ prelazno stanje
    m.setState(MachineState.STOPPING);
    machineRepository.save(m);

    // 2️⃣ async poziv (DRUGI BEAN)
    machineAsyncService.stopAsync(m.getId());

    // 3️⃣ instant response
    return toResponse(m);
  }



  public MachineResponse restart(Long id, String authEmail, Collection<?> perms) {
    Machine m = getAndAuthorize(id, authEmail, perms);

    if (m.getState() != MachineState.RUNNING) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "Machine not RUNNING"
      );
    }

    // 1️⃣ prelazno stanje
    m.setState(MachineState.RESTARTING);
    machineRepository.save(m);

    // 2️⃣ async poziv
    machineAsyncService.restartAsync(m.getId());

    // 3️⃣ instant response
    return toResponse(m);
  }












}
