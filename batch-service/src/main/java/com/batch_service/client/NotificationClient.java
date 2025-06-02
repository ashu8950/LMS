package com.batch_service.client;

import com.batch_service.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "notification-service")
public interface NotificationClient {

    @PostMapping("/notifications/send")
    void sendNotification(NotificationRequest request);
}
