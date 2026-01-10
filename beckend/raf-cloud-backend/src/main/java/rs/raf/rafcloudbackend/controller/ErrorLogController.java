package rs.raf.rafcloudbackend.controller;

import org.springframework.web.bind.annotation.*;
import rs.raf.rafcloudbackend.model.ErrorLog;
import rs.raf.rafcloudbackend.repository.ErrorLogRepository;

import java.util.List;

@RestController
@RequestMapping("/errors")
public class ErrorLogController {

  private final ErrorLogRepository errorLogRepository;

  public ErrorLogController(ErrorLogRepository errorLogRepository) {
    this.errorLogRepository = errorLogRepository;
  }

  @GetMapping
  public List<ErrorLog> getAllErrors() {
    return errorLogRepository.findAll();
  }
}
