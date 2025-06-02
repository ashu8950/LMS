package com.notification_service.dto;



import lombok.Data;
@Data
public class NotificationRequest {
	private String type;
    private String to;
    private String subject;
    private String body;
}
