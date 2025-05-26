package com.exam_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.exam_service.dto.ExamResultDTO;

@FeignClient(name = "notification-service")
public interface NotificationClient {
    @PostMapping("/notify/email")
    void sendExamResultNotification(ExamResultDTO result);
}
