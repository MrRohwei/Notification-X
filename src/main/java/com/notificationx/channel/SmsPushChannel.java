package com.notificationx.channel;

import com.notificationx.domain.PushChannelType;
import com.notificationx.domain.PushMessage;
import com.notificationx.domain.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class SmsPushChannel implements PushChannel {
  private final SmsClient smsClient;

  public SmsPushChannel(SmsClient smsClient) {
    this.smsClient = smsClient;
  }

  @Override
  public PushChannelType type() {
    return PushChannelType.SMS;
  }

  @Override
  public PushSendResult send(PushMessage message, UserProfile userProfile) {
    String phone = null;
    if (userProfile != null && userProfile.phone() != null && !userProfile.phone().isBlank()) {
      phone = userProfile.phone();
    } else if (message.recipient() != null && !message.recipient().isBlank()) {
      phone = message.recipient();
    }
    if (phone == null || phone.isBlank()) {
      return PushSendResult.failure("Missing phone number");
    }
    smsClient.send(phone, message.message());
    return PushSendResult.success();
  }
}
