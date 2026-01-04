package rs.raf.rafcloudbackend.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rs.raf.rafcloudbackend.filter.JwtFilter;

@Configuration
public class FilterConfig {
  @Bean
  public FilterRegistrationBean<JwtFilter> jwtFilterRegistration(JwtFilter jwtFilter) {
    FilterRegistrationBean<JwtFilter> reg = new FilterRegistrationBean<>();
    reg.setFilter(jwtFilter);
    reg.addUrlPatterns("/*");
    reg.setOrder(1);
    return reg;
  }
}
