package com.notificationx.domain;

public enum PushStatus {
  QUEUED("queued"),
  DELIVERED("delivered"),
  FAILED("failed");

  private final String value;

  PushStatus(String value) {
    this.value = value;
  }

  public String asString() {
    return value;
  }
}
