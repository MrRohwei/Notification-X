package com.notificationx.channel;

public record PushSendResult(boolean delivered, String error) {
  public static PushSendResult success() {
    return new PushSendResult(true, null);
  }

  public static PushSendResult failure(String error) {
    return new PushSendResult(false, error);
  }
}
