package com.notificationx.channel;

import com.notificationx.domain.PushChannelType;
import com.notificationx.domain.PushMessage;
import com.notificationx.domain.UserProfile;

public interface PushChannel {
  PushChannelType type();

  PushSendResult send(PushMessage message, UserProfile userProfile);
}
