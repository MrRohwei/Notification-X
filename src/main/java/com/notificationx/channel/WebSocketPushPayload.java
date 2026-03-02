package com.notificationx.channel;

import java.util.Map;

public record WebSocketPushPayload(
    String messageId,
    String message,
    Map<String, Object> extra
) {}
