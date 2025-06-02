package com.batch_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationRequest {
    private String type;    
    private String to;      
    private String subject;
    private String body;
}
