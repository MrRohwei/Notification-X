package com.notificationx.api;

import com.notificationx.config.PushProperties;
import com.notificationx.domain.PushStatusRecord;
import com.notificationx.dto.PushRequest;
import com.notificationx.dto.PushResponse;
import com.notificationx.dto.PushStatusResponse;
import com.notificationx.service.PushService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Validated
public class PushController {
  private final PushService pushService;
  private final PushProperties pushProperties;

  public PushController(PushService pushService, PushProperties pushProperties) {
    this.pushService = pushService;
    this.pushProperties = pushProperties;
  }

  @GetMapping("/push-channels")
  public Map<String, Object> listChannels() {
    Map<String, Object> response = new HashMap<>();
    response.put("channels", pushProperties.getChannels());
    return response;
  }

  @PostMapping("/push")
  public PushResponse push(@Valid @RequestBody PushRequest request) {
    return pushService.enqueue(request);
  }

  @GetMapping("/push/status/{messageId}")
  public PushStatusResponse status(@PathVariable String messageId) {
    PushStatusRecord record = pushService.getStatus(messageId);
    return new PushStatusResponse(record.status().asString(), record.error());
  }
}
