package rs.raf.rafcloudbackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "error_logs")
public class ErrorLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 1000)
  private String message;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String stackTrace;

  @Column(nullable = false)
  private LocalDateTime timestamp;

  public ErrorLog() {
  }

  public ErrorLog(String message, String stackTrace) {
    this.message = message;
    this.stackTrace = stackTrace;
    this.timestamp = LocalDateTime.now();
  }

  // GETTERS & SETTERS

  public Long getId() {
    return id;
  }

  public String getMessage() {
    return message;
  }

  public String getStackTrace() {
    return stackTrace;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }
}
