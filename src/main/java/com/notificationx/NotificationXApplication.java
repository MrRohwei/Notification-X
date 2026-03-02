package com.notificationx;

import com.notificationx.config.ExternalUserProperties;
import com.notificationx.config.PushProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({PushProperties.class, ExternalUserProperties.class})
public class NotificationXApplication {
  public static void main(String[] args) {
    SpringApplication.run(NotificationXApplication.class, args);
  }
}
