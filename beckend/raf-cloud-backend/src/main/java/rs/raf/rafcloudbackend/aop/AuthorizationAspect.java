package rs.raf.rafcloudbackend.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import rs.raf.rafcloudbackend.model.Permission;
import rs.raf.rafcloudbackend.filter.JwtFilter;

import java.util.Collection;

@Aspect
@Component
public class AuthorizationAspect {
  private final HttpServletRequest request;

  public AuthorizationAspect(HttpServletRequest request) {
    this.request = request;
  }

  @Before("@annotation(requiresPermission)")
  public void check(JoinPoint jp, RequiresPermission requiresPermission) {
    Object permsObj = request.getAttribute(JwtFilter.REQ_ATTR_PERMS);

    if (permsObj == null) {
      throw new RuntimeException("Unauthorized");
    }

    Permission needed = requiresPermission.value();

    if (permsObj instanceof Collection<?> c) {
      boolean ok = c.stream().anyMatch(p -> needed.name().equals(String.valueOf(p)));
      if (!ok) {
        throw new RuntimeException("Forbidden");
      }
      return;
    }

    throw new RuntimeException("Forbidden");
  }
}
