package com.notificationx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "external-user")
public class ExternalUserProperties {
  private String baseUrl;
  private String userPath = "/api/v1/users/{id}";
  private String authHeader = "Authorization";
  private String authToken;
  private Duration timeout = Duration.ofSeconds(2);

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String getUserPath() {
    return userPath;
  }

  public void setUserPath(String userPath) {
    this.userPath = userPath;
  }

  public String getAuthHeader() {
    return authHeader;
  }

  public void setAuthHeader(String authHeader) {
    this.authHeader = authHeader;
  }

  public String getAuthToken() {
    return authToken;
  }

  public void setAuthToken(String authToken) {
    this.authToken = authToken;
  }

  public Duration getTimeout() {
    return timeout;
  }

  public void setTimeout(Duration timeout) {
    this.timeout = timeout;
  }
}
