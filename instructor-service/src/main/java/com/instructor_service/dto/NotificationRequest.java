package com.instructor_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
 private String to;          // recipient email address
 private String subject;     // email subject
 private String message;     // email body/message content
}
