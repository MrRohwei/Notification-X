package com.notificationx.domain;

public enum PushChannelType {
  WEBSOCKET("websocket"),
  SMS("sms");

  private final String value;

  PushChannelType(String value) {
    this.value = value;
  }

  public String asString() {
    return value;
  }

  public static PushChannelType from(String raw) {
    if (raw == null || raw.isBlank()) {
      throw new IllegalArgumentException("Channel is required");
    }
    String normalized = raw.trim().toLowerCase();
    for (PushChannelType type : values()) {
      if (type.value.equals(normalized)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unsupported channel: " + raw);
  }
}
