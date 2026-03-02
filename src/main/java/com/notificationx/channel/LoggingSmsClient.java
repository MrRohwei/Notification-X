package com.notificationx.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingSmsClient implements SmsClient {
  private static final Logger log = LoggerFactory.getLogger(LoggingSmsClient.class);

  @Override
  public void send(String phone, String message) {
    log.info("SMS sent to {}: {}", phone, message);
  }
}
