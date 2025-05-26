package com.student_service.feign_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.student_service.dto.NotificationRequest;

@FeignClient(name = "notification-service",url = "http://localhost:6060")
public interface NotificationClient {
	@PostMapping("/notifications/send")
    void sendEmail(@RequestBody NotificationRequest request);
}
