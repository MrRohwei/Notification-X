package com.notificationx.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserProfile(
    String id,
    String phone,
    String email,
    String name
) {
  public static UserProfile minimal(String id) {
    return new UserProfile(id, null, null, null);
  }
}
