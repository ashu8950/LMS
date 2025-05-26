package com.notification_service.service;

import com.notification_service.dto.NotificationRequest;

public interface NotificationService {
    void sendHtmlEmail(NotificationRequest request);
}
