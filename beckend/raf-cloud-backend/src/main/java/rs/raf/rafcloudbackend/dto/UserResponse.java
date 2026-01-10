package rs.raf.rafcloudbackend.dto;

import java.util.Set;

public class UserResponse {

  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private Set<String> permissions;
  private Long version;

  public UserResponse() {}

  public UserResponse(Long id, String firstName, String lastName, String email,
                      Set<String> permissions, Long version) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.permissions = permissions;
    this.version = version;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }

  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public Set<String> getPermissions() { return permissions; }
  public void setPermissions(Set<String> permissions) { this.permissions = permissions; }

  public Long getVersion() { return version; }
  public void setVersion(Long version) { this.version = version; }
}
