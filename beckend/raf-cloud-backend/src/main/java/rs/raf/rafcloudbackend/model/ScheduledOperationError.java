package rs.raf.rafcloudbackend.model;

import jakarta.persistence.*;
import rs.raf.rafcloudbackend.model.ScheduledOperationType;

import java.time.LocalDateTime;

@Entity
public class ScheduledOperationError {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private LocalDateTime occurredAt;

  @Column(nullable = false)
  private Long machineId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ScheduledOperationType operation;

  @Column(nullable = false, length = 1024)
  private String message;

  public ScheduledOperationError() {}

  public Long getId() {
    return id;
  }

  public LocalDateTime getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(LocalDateTime occurredAt) {
    this.occurredAt = occurredAt;
  }

  public Long getMachineId() {
    return machineId;
  }

  public void setMachineId(Long machineId) {
    this.machineId = machineId;
  }

  public ScheduledOperationType getOperation() {
    return operation;
  }

  public void setOperation(ScheduledOperationType operation) {
    this.operation = operation;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
