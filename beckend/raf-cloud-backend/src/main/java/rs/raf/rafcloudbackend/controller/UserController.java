package rs.raf.rafcloudbackend.controller;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import rs.raf.rafcloudbackend.aop.RequiresPermission;
import rs.raf.rafcloudbackend.dto.CreateUserRequest;
import rs.raf.rafcloudbackend.dto.UpdateUserRequest;
import rs.raf.rafcloudbackend.dto.UserResponse;
import rs.raf.rafcloudbackend.model.Permission;
import rs.raf.rafcloudbackend.service.UserService;

import java.util.List;
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @RequiresPermission(Permission.READ_USER)
  public List<UserResponse> all() {
    return userService.getAll();
  }

  @PostMapping
  @RequiresPermission(Permission.CREATE_USER)
  public UserResponse create(@Valid @RequestBody CreateUserRequest req) {
    return userService.create(req);
  }

  @PutMapping("/{id}")
  @RequiresPermission(Permission.UPDATE_USER)
  public UserResponse update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest req) {
    return userService.update(id, req);
  }

  @DeleteMapping("/{id}")
  @RequiresPermission(Permission.DELETE_USER)
  public void delete(@PathVariable Long id) {
    userService.delete(id);
  }

}
