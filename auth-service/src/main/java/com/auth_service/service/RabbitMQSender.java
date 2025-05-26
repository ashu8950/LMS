package com.auth_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQSender {

    private final RabbitTemplate rabbitTemplate;
    private final EmailService emailService;

    /**
     * Send OTP message to RabbitMQ and email.
     */
    public void sendOtpMessage(String email, String otp) {
        String message = email + ":" + otp;
        rabbitTemplate.convertAndSend("otp_queue", message);
        log.info("OTP message sent to queue for: {}", email);

        emailService.sendVerificationOtp(email, otp);
    }

    /**
     * Send welcome message via email.
     */
    public void sendWelcomeMessage(String email, String name) {
        emailService.sendWelcomeMessage(email, name);
        log.info("Welcome email sent to: {}", email);
    }
}
