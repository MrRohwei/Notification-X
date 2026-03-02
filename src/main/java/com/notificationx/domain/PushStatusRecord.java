package com.notificationx.domain;

import java.time.Instant;

public record PushStatusRecord(
    String messageId,
    PushStatus status,
    String error,
    Instant updatedAt
) {}
