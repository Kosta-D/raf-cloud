package rs.raf.rafcloudbackend.dto;

public class MachineWsDto {

  private Long id;
  private String name;
  private String state;

  public MachineWsDto(Long id, String name, String state) {
    this.id = id;
    this.name = name;
    this.state = state;
  }

  public Long getId() { return id; }
  public String getName() { return name; }
  public String getState() { return state; }
}
