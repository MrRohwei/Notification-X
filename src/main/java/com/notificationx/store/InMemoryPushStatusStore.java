package com.notificationx.store;

import com.notificationx.domain.PushMessage;
import com.notificationx.domain.PushStatus;
import com.notificationx.domain.PushStatusRecord;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class InMemoryPushStatusStore implements PushStatusStore {
  private final ConcurrentMap<String, PushStatusRecord> statuses = new ConcurrentHashMap<>();

  @Override
  public void create(PushMessage message) {
    statuses.put(message.id(), new PushStatusRecord(message.id(), PushStatus.QUEUED, null, Instant.now()));
  }

  @Override
  public void markDelivered(String messageId) {
    update(messageId, PushStatus.DELIVERED, null);
  }

  @Override
  public void markFailed(String messageId, String error) {
    update(messageId, PushStatus.FAILED, error);
  }

  @Override
  public PushStatusRecord get(String messageId) {
    PushStatusRecord record = statuses.get(messageId);
    if (record == null) {
      throw new NoSuchElementException("Message not found: " + messageId);
    }
    return record;
  }

  private void update(String messageId, PushStatus status, String error) {
    statuses.compute(messageId, (key, existing) -> new PushStatusRecord(key, status, error, Instant.now()));
  }
}
