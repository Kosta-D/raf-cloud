package rs.raf.rafcloudbackend.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateMachineRequest {
  @NotBlank
  private String name;

  public CreateMachineRequest() {}

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
}

