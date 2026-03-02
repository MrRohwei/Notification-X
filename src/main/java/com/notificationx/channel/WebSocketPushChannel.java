package com.notificationx.channel;

import com.notificationx.domain.PushChannelType;
import com.notificationx.domain.PushMessage;
import com.notificationx.domain.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketPushChannel implements PushChannel {
  private static final Logger log = LoggerFactory.getLogger(WebSocketPushChannel.class);
  private final SimpMessagingTemplate messagingTemplate;

  public WebSocketPushChannel(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  @Override
  public PushChannelType type() {
    return PushChannelType.WEBSOCKET;
  }

  @Override
  public PushSendResult send(PushMessage message, UserProfile userProfile) {
    String destinationId = userProfile != null && userProfile.id() != null && !userProfile.id().isBlank()
        ? userProfile.id()
        : message.recipient();
    if (destinationId == null || destinationId.isBlank()) {
      return PushSendResult.failure("Missing websocket recipient");
    }
    WebSocketPushPayload payload = new WebSocketPushPayload(message.id(), message.message(), message.extra());
    String destination = "/topic/notifications/" + destinationId;
    log.info("WebSocket push to {} (messageId={})", destination, message.id());
    messagingTemplate.convertAndSend(destination, payload);
    return PushSendResult.success();
  }
}
