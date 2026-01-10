package rs.raf.rafcloudbackend.dto;

import rs.raf.rafcloudbackend.model.ScheduledOperationType;

import java.time.LocalDateTime;

public class ScheduleOperationRequest {

  private Long machineId;
  private ScheduledOperationType operation;
  private LocalDateTime scheduledAt;

  public ScheduleOperationRequest() {}

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

  public LocalDateTime getScheduledAt() {
    return scheduledAt;
  }

  public void setScheduledAt(LocalDateTime scheduledAt) {
    this.scheduledAt = scheduledAt;
  }
}
