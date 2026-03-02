package com.notificationx.domain;

import java.time.Instant;
import java.util.Map;

public record PushMessage(
    String id,
    PushChannelType channel,
    String recipient,
    String message,
    Map<String, Object> extra,
    Instant createdAt
) {}
