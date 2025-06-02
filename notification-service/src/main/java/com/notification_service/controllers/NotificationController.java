package com.notification_service.controllers;

import com.notification_service.config.RabbitConfig;
import com.notification_service.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Send a notification message into RabbitMQ. The "type" field
     * determines which routing key → queue is used.
     *
     * Example JSON body:
     * {
     *   "type": "schedule",
     *   "to":   "user@example.com",
     *   "subject": "Your appointment is tomorrow",
     *   "body": "<h1>Reminder</h1><p>Don’t forget…</p>"
     * }
     */
    @PostMapping("/send")
    public ResponseEntity<Void> sendNotification(@RequestBody NotificationRequest req) {
        String routingKey;

        switch (req.getType()) {
            case "schedule":
                routingKey = RabbitConfig.SCHEDULE_ROUTING_KEY; // "notification.schedule"
                break;
            case "feedback":
                routingKey = RabbitConfig.FEEDBACK_ROUTING_KEY; // "notification.feedback"
                break;
            case "payment":
                routingKey = RabbitConfig.PAYMENT_ROUTING_KEY;  // "notification.payment"
                break;
            case "otp":
                routingKey = RabbitConfig.OTP_ROUTING_KEY;      // "notification.otp"
                break;
            case "REGISTER":
                routingKey = RabbitConfig.REGISTER_ROUTING_KEY; // define this in RabbitConfig
                break;
            case "PROFILE_UPDATE":
                routingKey = RabbitConfig.PROFILE_UPDATE_ROUTING_KEY;
                break;
            case "ENROLLMENT_CONFIRMATION":
                routingKey = RabbitConfig.ENROLLMENT_CONFIRMATION_ROUTING_KEY;
                break;
            case "FEEDBACK_RECEIVED":
                routingKey = RabbitConfig.FEEDBACK_RECEIVED_ROUTING_KEY;
                break;
            default:
                throw new IllegalArgumentException("Unknown notification type: " + req.getType());
        }

        
        rabbitTemplate.convertAndSend(
            RabbitConfig.NOTIFICATION_EXCHANGE, // exchange = "notification.exchange"
            routingKey,
            req
        );

        return ResponseEntity.ok().build();
    }
}
