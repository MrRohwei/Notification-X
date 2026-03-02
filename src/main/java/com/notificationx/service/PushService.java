package com.notificationx.service;

import com.notificationx.channel.PushChannel;
import com.notificationx.channel.PushSendResult;
import com.notificationx.client.ExternalUserClient;
import com.notificationx.config.PushProperties;
import com.notificationx.domain.PushChannelType;
import com.notificationx.domain.PushMessage;
import com.notificationx.domain.PushStatus;
import com.notificationx.domain.PushStatusRecord;
import com.notificationx.domain.UserProfile;
import com.notificationx.dto.PushRequest;
import com.notificationx.dto.PushResponse;
import com.notificationx.store.PushStatusStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class PushService {
  private static final Logger log = LoggerFactory.getLogger(PushService.class);
  private final PushChannelRegistry channelRegistry;
  private final PushStatusStore statusStore;
  private final ExternalUserClient externalUserClient;
  private final TaskExecutor taskExecutor;
  private final PushProperties pushProperties;

  public PushService(PushChannelRegistry channelRegistry,
                     PushStatusStore statusStore,
                     ExternalUserClient externalUserClient,
                     @Qualifier("pushTaskExecutor") TaskExecutor pushTaskExecutor,
                     PushProperties pushProperties) {
    this.channelRegistry = channelRegistry;
    this.statusStore = statusStore;
    this.externalUserClient = externalUserClient;
    this.taskExecutor = pushTaskExecutor;
    this.pushProperties = pushProperties;
  }

  public PushResponse enqueue(PushRequest request) {
    PushChannelType channelType = PushChannelType.from(request.channel());
    if (!pushProperties.isEnabled(channelType)) {
      throw new IllegalArgumentException("Channel disabled: " + request.channel());
    }

    String messageId = UUID.randomUUID().toString();
    Map<String, Object> extra = request.extra() == null ? Map.of() : request.extra();
    PushMessage message = new PushMessage(
        messageId,
        channelType,
        request.recipient().trim(),
        request.message(),
        extra,
        Instant.now()
    );

    statusStore.create(message);
    taskExecutor.execute(() -> deliver(message));
    return new PushResponse(PushStatus.QUEUED.asString(), messageId);
  }

  public PushStatusRecord getStatus(String messageId) {
    return statusStore.get(messageId);
  }

  private void deliver(PushMessage message) {
    try {
      PushChannel channel = channelRegistry.get(message.channel());
      if (channel == null) {
        statusStore.markFailed(message.id(), "Unsupported channel");
        return;
      }
      UserProfile userProfile = externalUserClient.fetchUser(message.recipient());
      PushSendResult result = channel.send(message, userProfile);
      if (result.delivered()) {
        statusStore.markDelivered(message.id());
      } else {
        statusStore.markFailed(message.id(), result.error());
      }
    } catch (Exception ex) {
      log.error("Push delivery failed (messageId={})", message.id(), ex);
      statusStore.markFailed(message.id(), ex.getMessage());
    }
  }
}
