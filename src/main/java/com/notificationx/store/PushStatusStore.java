package com.notificationx.store;

import com.notificationx.domain.PushMessage;
import com.notificationx.domain.PushStatusRecord;

public interface PushStatusStore {
  void create(PushMessage message);

  void markDelivered(String messageId);

  void markFailed(String messageId, String error);

  PushStatusRecord get(String messageId);
}
