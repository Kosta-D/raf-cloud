package rs.raf.rafcloudbackend.dto;

import java.util.Set;
public class LoginResponse {
  private String token;
  private Set<String> permissions;

  public LoginResponse() {}

  public LoginResponse(String token, Set<String> permissions) {
    this.token = token;
    this.permissions = permissions;
  }

  public String getToken() { return token; }
  public void setToken(String token) { this.token = token; }

  public Set<String> getPermissions() { return permissions; }
  public void setPermissions(Set<String> permissions) { this.permissions = permissions; }

}
