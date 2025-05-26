package com.auth_service.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQReceiver {

    @RabbitListener(queues = "otp_queue")
    public void receiveOtpMessage(String message) {
        System.out.println("Received OTP message: " + message);
        // You can add code here to send SMS/email or save OTP etc.
    }
}
