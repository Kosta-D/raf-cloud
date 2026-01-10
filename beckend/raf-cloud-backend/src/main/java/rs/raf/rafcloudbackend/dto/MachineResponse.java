package rs.raf.rafcloudbackend.dto;

import rs.raf.rafcloudbackend.model.MachineState;

import java.time.LocalDateTime;

public class MachineResponse {
  private Long id;
  private String name;
  private boolean active;
  private MachineState state;
  private LocalDateTime createdAt;
  private String createdByEmail;

  public MachineResponse() {}

  public MachineResponse(Long id, String name, boolean active, MachineState state,
                         LocalDateTime createdAt, String createdByEmail) {
    this.id = id;
    this.name = name;
    this.active = active;
    this.state = state;
    this.createdAt = createdAt;
    this.createdByEmail = createdByEmail;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public boolean isActive() { return active; }
  public void setActive(boolean active) { this.active = active; }

  public MachineState getState() { return state; }
  public void setState(MachineState state) { this.state = state; }

  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

  public String getCreatedByEmail() { return createdByEmail; }
  public void setCreatedByEmail(String createdByEmail) { this.createdByEmail = createdByEmail; }
}
