package rs.raf.rafcloudbackend.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import rs.raf.rafcloudbackend.repository.UserRepository;
import rs.raf.rafcloudbackend.util.JwtUtil;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter{

  public static final String REQ_ATTR_EMAIL = "auth_email";
  public static final String REQ_ATTR_PERMS = "auth_permissions";

  private final JwtUtil jwtUtil;

  public JwtFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    return path.startsWith("/auth/") || path.startsWith("/h2");
  }
  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {

    String header = request.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = header.substring(7);
    try {
      Jws<Claims> parsed = jwtUtil.parse(token);
      String email = parsed.getBody().getSubject();

      request.setAttribute(REQ_ATTR_EMAIL, email);
      request.setAttribute(REQ_ATTR_PERMS, parsed.getBody().get("permissions"));

      filterChain.doFilter(request, response);
    } catch (Exception e) {
      response.setStatus(401);
      response.getWriter().write("Invalid token");
    }
  }
}
