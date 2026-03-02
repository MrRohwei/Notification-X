package com.notificationx.client;

import com.notificationx.config.ExternalUserProperties;
import com.notificationx.domain.UserProfile;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExternalUserClient {
  private final ExternalUserProperties properties;
  private final WebClient webClient;

  public ExternalUserClient(ExternalUserProperties properties, WebClient.Builder builder) {
    this.properties = properties;
    WebClient.Builder configured = builder;
    if (properties.getBaseUrl() != null && !properties.getBaseUrl().isBlank()) {
      configured = builder.baseUrl(properties.getBaseUrl());
    }
    this.webClient = configured.build();
  }

  public UserProfile fetchUser(String recipient) {
    if (recipient == null || recipient.isBlank()) {
      throw new IllegalArgumentException("Recipient is required");
    }
    if (properties.getBaseUrl() == null || properties.getBaseUrl().isBlank()) {
      return UserProfile.minimal(recipient);
    }

    WebClient.RequestHeadersSpec<?> request = webClient.get()
        .uri(properties.getUserPath(), recipient);

    if (properties.getAuthToken() != null && !properties.getAuthToken().isBlank()) {
      request = request.header(properties.getAuthHeader(), properties.getAuthToken());
    }

    try {
      UserProfile profile = request.retrieve()
          .bodyToMono(UserProfile.class)
          .timeout(properties.getTimeout())
          .block();
      if (profile == null) {
        throw new IllegalStateException("User not found: " + recipient);
      }
      return profile;
    } catch (Exception ex) {
      throw new IllegalStateException("Failed to fetch user: " + ex.getMessage(), ex);
    }
  }
}
