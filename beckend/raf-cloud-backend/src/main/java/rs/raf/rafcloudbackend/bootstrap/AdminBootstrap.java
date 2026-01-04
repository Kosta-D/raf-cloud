package rs.raf.rafcloudbackend.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import rs.raf.rafcloudbackend.model.Permission;
import rs.raf.rafcloudbackend.model.User;
import rs.raf.rafcloudbackend.repository.UserRepository;

import java.util.EnumSet;

@Component
public class AdminBootstrap implements CommandLineRunner {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  public AdminBootstrap(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(String... args) {
    if (userRepository.existsByEmail("admin@raf.rs")) {
      return;
    }

    User admin = new User();
    admin.setFirstName("Admin");
    admin.setLastName("Admin");
    admin.setEmail("admin@raf.rs");
    admin.setPassword(passwordEncoder.encode("admin"));
    admin.setPermissions(EnumSet.allOf(Permission.class));

    userRepository.save(admin);
  }
}
