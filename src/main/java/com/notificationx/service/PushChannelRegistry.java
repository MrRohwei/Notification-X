package com.notificationx.service;

import com.notificationx.channel.PushChannel;
import com.notificationx.domain.PushChannelType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class PushChannelRegistry {
  private final Map<PushChannelType, PushChannel> channels = new EnumMap<>(PushChannelType.class);

  public PushChannelRegistry(List<PushChannel> channelList) {
    if (channelList != null) {
      for (PushChannel channel : channelList) {
        channels.put(channel.type(), channel);
      }
    }
  }

  public PushChannel get(PushChannelType type) {
    return channels.get(type);
  }
}
