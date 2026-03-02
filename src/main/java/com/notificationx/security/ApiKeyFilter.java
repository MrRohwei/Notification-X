package com.notificationx.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {
  private final String apiKey;

  public ApiKeyFilter(@Value("${security.api-key:}") String apiKey) {
    this.apiKey = apiKey;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (apiKey == null || apiKey.isBlank()) {
      filterChain.doFilter(request, response);
      return;
    }
    String header = request.getHeader("X-API-Key");
    if (apiKey.equals(header)) {
      filterChain.doFilter(request, response);
      return;
    }
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.getWriter().write("{\"message\":\"Unauthorized\"}");
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    return path.startsWith("/actuator") || path.startsWith("/ws");
  }
}
