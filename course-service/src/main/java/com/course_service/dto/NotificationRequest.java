package com.course_service.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private String to;
    private String subject;
    private String body;
}
