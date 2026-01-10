package rs.raf.rafcloudbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import rs.raf.rafcloudbackend.aop.RequiresPermission;
import rs.raf.rafcloudbackend.dto.CreateMachineRequest;
import rs.raf.rafcloudbackend.dto.MachineResponse;
import rs.raf.rafcloudbackend.dto.ScheduleOperationRequest;
import rs.raf.rafcloudbackend.filter.JwtFilter;
import rs.raf.rafcloudbackend.model.Machine;
import rs.raf.rafcloudbackend.model.MachineState;
import rs.raf.rafcloudbackend.model.Permission;
import rs.raf.rafcloudbackend.model.ScheduledOperation;
import rs.raf.rafcloudbackend.model.User;
import rs.raf.rafcloudbackend.model.ScheduledOperationStatus;
import rs.raf.rafcloudbackend.repository.ScheduledOperationRepository;
import rs.raf.rafcloudbackend.service.MachineService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/machines")
public class MachineController {

  private final MachineService machineService;
  private final ScheduledOperationRepository scheduledOperationRepository;
  private final HttpServletRequest request;

  public MachineController(
    MachineService machineService,
    ScheduledOperationRepository scheduledOperationRepository,
    HttpServletRequest request
  ) {
    this.machineService = machineService;
    this.scheduledOperationRepository = scheduledOperationRepository;
    this.request = request;
  }

  // ========================
  // CREATE MACHINE
  // ========================
  @PostMapping
  @RequiresPermission(Permission.CREATE_MACHINE)
  public MachineResponse create(@Valid @RequestBody CreateMachineRequest req) {
    String email = email();
    return machineService.create(req, email);
  }

  // ========================
  // SEARCH MACHINES
  // ========================
  @GetMapping
  @RequiresPermission(Permission.SEARCH_MACHINE)
  public List<MachineResponse> search(
    @RequestParam(required = false) String name,
    @RequestParam(required = false) List<MachineState> states,
    @RequestParam(required = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate createdFrom,
    @RequestParam(required = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate createdTo
  ) {
    Set<MachineState> stateSet = (states == null)
      ? Set.of()
      : new HashSet<>(states);

    return machineService.search(
      name,
      stateSet,
      createdFrom,
      createdTo,
      email(),
      perms()
    );
  }

  // ========================
  // DESTROY MACHINE
  // ========================
  @DeleteMapping("/{id}")
  @RequiresPermission(Permission.DESTROY_MACHINE)
  public void destroy(@PathVariable Long id) {
    machineService.destroy(id, email(), perms());
  }

  // ========================
  // START / STOP / RESTART (ODMAH)
  // ========================
  @PostMapping("/{id}/start")
  @RequiresPermission(Permission.START_MACHINE)
  public MachineResponse start(@PathVariable Long id) {
    return machineService.start(id, email(), perms());
  }

  @PostMapping("/{id}/stop")
  @RequiresPermission(Permission.STOP_MACHINE)
  public MachineResponse stop(@PathVariable Long id) {
    return machineService.stop(id, email(), perms());
  }

  @PostMapping("/{id}/restart")
  @RequiresPermission(Permission.RESTART_MACHINE)
  public MachineResponse restart(@PathVariable Long id) {
    return machineService.restart(id, email(), perms());
  }

  // ========================
  // ⭐ SCHEDULE OPERATION ⭐
  // ========================
  @PostMapping("/schedule")
  @RequiresPermission(Permission.START_MACHINE)
  public void schedule(@RequestBody ScheduleOperationRequest req) {

    ScheduledOperation op = new ScheduledOperation();
    op.setMachineId(req.getMachineId());
    op.setOperation(req.getOperation());
    op.setScheduledAt(req.getScheduledAt());
    op.setStatus(ScheduledOperationStatus.PENDING);
    op.setCreatedByEmail(email());
    op.setCreatedAt(LocalDateTime.now());

    scheduledOperationRepository.save(op);
  }


  // ========================
  // HELPERS
  // ========================
  private String email() {
    return (String) request.getAttribute(JwtFilter.REQ_ATTR_EMAIL);
  }

  private Collection<?> perms() {
    return (Collection<?>) request.getAttribute(JwtFilter.REQ_ATTR_PERMS);
  }
}
