package com.notificationx.channel;

public interface SmsClient {
  void send(String phone, String message);
}
