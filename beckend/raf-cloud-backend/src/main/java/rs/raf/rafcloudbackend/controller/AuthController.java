package rs.raf.rafcloudbackend.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import rs.raf.rafcloudbackend.dto.LoginRequest;
import rs.raf.rafcloudbackend.dto.LoginResponse;
import rs.raf.rafcloudbackend.model.User;
import rs.raf.rafcloudbackend.repository.UserRepository;
import rs.raf.rafcloudbackend.util.JwtUtil;
import rs.raf.rafcloudbackend.service.AuthService;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
  private final AuthService authService;
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  public AuthController(AuthService authService, UserRepository userRepository, JwtUtil jwtUtil) {
    this.authService = authService;
    this.userRepository = userRepository;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/login")
  public LoginResponse login(@Valid @RequestBody LoginRequest request) {
    String token = authService.loginAndGetToken(request.getEmail(), request.getPassword());

    User u = userRepository.findByEmail(request.getEmail()).orElseThrow();
    Set<String> perms = u.getPermissions().stream().map(Enum::name).collect(Collectors.toSet());

    return new LoginResponse(token, perms);
  }
}
