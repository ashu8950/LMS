package com.course_service.feign;




import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.course_service.dto.NotificationRequest;

@FeignClient(name = "notification-service",url = "http://localhost:6060")
public interface NotificationClient {

    @PostMapping("/api/notifications/send")
    void sendNotification(NotificationRequest request);
}
