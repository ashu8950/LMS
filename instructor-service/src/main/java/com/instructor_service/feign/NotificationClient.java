package com.instructor_service.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.instructor_service.dto.NotificationRequest;

@FeignClient(name = "notification-service")
public interface NotificationClient {
    @PostMapping("/notify/email")
    void sendEmail(NotificationRequest request);
}