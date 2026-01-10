package rs.raf.rafcloudbackend.model;


import jakarta.persistence.*;
import rs.raf.rafcloudbackend.model.ScheduledOperationStatus;
import rs.raf.rafcloudbackend.model.ScheduledOperationType;

import java.time.LocalDateTime;

@Entity
public class ScheduledOperation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long machineId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ScheduledOperationType operation;

  @Column(nullable = false)
  private LocalDateTime scheduledAt;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ScheduledOperationStatus status;

  @Column(nullable = false)
  private String createdByEmail;


  @Column(nullable = false)
  private LocalDateTime createdAt;

  public ScheduledOperation() {}

  public Long getId() {
    return id;
  }

  public Long getMachineId() {
    return machineId;
  }

  public void setMachineId(Long machineId) {
    this.machineId = machineId;
  }


  public String getCreatedByEmail() {
    return createdByEmail;
  }

  public void setCreatedByEmail(String createdByEmail) {
    this.createdByEmail = createdByEmail;
  }



  public ScheduledOperationType getOperation() {
    return operation;
  }

  public void setOperation(ScheduledOperationType operation) {
    this.operation = operation;
  }

  public LocalDateTime getScheduledAt() {
    return scheduledAt;
  }

  public void setScheduledAt(LocalDateTime scheduledAt) {
    this.scheduledAt = scheduledAt;
  }

  public ScheduledOperationStatus getStatus() {
    return status;
  }

  public void setStatus(ScheduledOperationStatus status) {
    this.status = status;
  }


  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
