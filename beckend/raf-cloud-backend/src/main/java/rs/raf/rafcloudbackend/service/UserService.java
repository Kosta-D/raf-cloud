package rs.raf.rafcloudbackend.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rs.raf.rafcloudbackend.dto.CreateUserRequest;
import rs.raf.rafcloudbackend.dto.UpdateUserRequest;
import rs.raf.rafcloudbackend.dto.UserResponse;
import rs.raf.rafcloudbackend.model.User;
import rs.raf.rafcloudbackend.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class UserService {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public List<UserResponse> getAll() {
    return userRepository.findAll().stream()
      .map(this::toResponse)
      .collect(Collectors.toList());
  }

  public UserResponse create(CreateUserRequest req) {
    if (userRepository.existsByEmail(req.getEmail())) {
      throw new RuntimeException("Email already exists");
    }

    User u = new User();
    u.setFirstName(req.getFirstName());
    u.setLastName(req.getLastName());
    u.setEmail(req.getEmail());
    u.setPassword(passwordEncoder.encode(req.getPassword()));
    u.setPermissions(req.getPermissions());

    return toResponse(userRepository.save(u));
  }

  public UserResponse update(Long id, UpdateUserRequest req) {
    User u = userRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("User not found"));

    // email uniqueness ako se menja na tuđi
    if (!u.getEmail().equals(req.getEmail()) && userRepository.existsByEmail(req.getEmail())) {
      throw new RuntimeException("Email already exists");
    }

    u.setFirstName(req.getFirstName());
    u.setLastName(req.getLastName());
    u.setEmail(req.getEmail());

    if (req.getPassword() != null && !req.getPassword().isBlank()) {
      u.setPassword(passwordEncoder.encode(req.getPassword()));
    }

    u.setPermissions(req.getPermissions());

    return toResponse(userRepository.save(u));
  }

  public void delete(Long id) {
    if (!userRepository.existsById(id)) {
      throw new RuntimeException("User not found");
    }
    userRepository.deleteById(id);
  }

  private UserResponse toResponse(User u) {
    Set<String> perms = u.getPermissions().stream()
      .map(Enum::name)
      .collect(Collectors.toSet());

    return new UserResponse(u.getId(), u.getFirstName(), u.getLastName(), u.getEmail(), perms);
  }
}
