package com.pulsefit.subscription.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.pulsefit.subscription.config.FeignClientConfig;
import com.pulsefit.subscription.dto.NotificationRequest;

@FeignClient(
        name = "notification-service",
        configuration = FeignClientConfig.class
)
public interface NotificationClient {

    @PostMapping("/api/notifications")
    void createNotification(@RequestBody NotificationRequest notification);
}