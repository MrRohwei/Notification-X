package com.notificationx.config;

import com.notificationx.domain.PushChannelType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "push")
public class PushProperties {
  private List<String> channels = new ArrayList<>(List.of("websocket", "sms"));

  public List<String> getChannels() {
    return channels;
  }

  public void setChannels(List<String> channels) {
    if (channels == null) {
      this.channels = new ArrayList<>();
      return;
    }
    List<String> normalized = new ArrayList<>();
    for (String channel : channels) {
      if (channel == null) {
        continue;
      }
      normalized.add(channel.trim().toLowerCase());
    }
    this.channels = normalized;
  }

  public boolean isEnabled(PushChannelType type) {
    return type != null && channels.contains(type.asString());
  }
}
