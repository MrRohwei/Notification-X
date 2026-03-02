package com.notificationx.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public record PushRequest(
    @NotBlank String channel,
    @NotBlank String recipient,
    @NotBlank String message,
    Map<String, Object> extra
) {}
