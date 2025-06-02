package com.notification_service.service.impl;

import com.notification_service.config.RabbitConfig;
import com.notification_service.dto.NotificationRequest;
import com.notification_service.service.NotificationService;
import com.notification_service.util.HtmlEmailBuilder;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;

    @Async
    @Override
    public void sendHtmlEmail(NotificationRequest req) {
        if (req == null || req.getTo() == null || req.getTo().isBlank()
                || req.getSubject() == null || req.getSubject().isBlank()
                || req.getBody() == null) {
            log.warn("Invalid NotificationRequest: {}", req);
            return;
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setTo(req.getTo());
            helper.setSubject(req.getSubject());

            String finalHtml = HtmlEmailBuilder.build(req.getBody());
            helper.setText(finalHtml, true);

            mailSender.send(mimeMessage);
            log.info("Email successfully sent to {}", req.getTo());
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", req.getTo(), e.getMessage(), e);
        }
    }

    @RabbitListener(queues = RabbitConfig.SCHEDULE_QUEUE)
    public void handleScheduleEvent(NotificationRequest request) {
        log.info("[schedule.queue] Received event for recipient={}", request.getTo());
        sendHtmlEmail(request);
    }

    @RabbitListener(queues = RabbitConfig.FEEDBACK_QUEUE)
    public void handleFeedbackEvent(NotificationRequest request) {
        log.info("[feedback.queue] Received event for recipient={}", request.getTo());
        sendHtmlEmail(request);
    }

    @RabbitListener(queues = RabbitConfig.PAYMENT_QUEUE)
    public void handlePaymentSuccessEvent(NotificationRequest request) {
        log.info("[payment.queue] Received event for recipient={}", request.getTo());
        sendHtmlEmail(request);
    }

    @RabbitListener(queues = RabbitConfig.OTP_QUEUE)
    public void handleOtpEvent(NotificationRequest request) {
        log.info("[otp.queue] Received event for recipient={}", request.getTo());
        sendHtmlEmail(request);
    }

    @RabbitListener(queues = RabbitConfig.REGISTER_QUEUE)
    public void handleRegisterEvent(NotificationRequest request) {
        log.info("[register.queue] Received event for recipient={}", request.getTo());
        sendHtmlEmail(request);
    }

    @RabbitListener(queues = RabbitConfig.PROFILE_UPDATE_QUEUE)
    public void handleProfileUpdateEvent(NotificationRequest request) {
        log.info("[profile_update.queue] Received event for recipient={}", request.getTo());
        sendHtmlEmail(request);
    }

    @RabbitListener(queues = RabbitConfig.ENROLLMENT_CONFIRMATION_QUEUE)
    public void handleEnrollmentConfirmationEvent(NotificationRequest request) {
        log.info("[enrollment_confirmation.queue] Received event for recipient={}", request.getTo());
        sendHtmlEmail(request);
    }

    @RabbitListener(queues = RabbitConfig.FEEDBACK_RECEIVED_QUEUE)
    public void handleFeedbackReceivedEvent(NotificationRequest request) {
        log.info("[feedback_received.queue] Received event for recipient={}", request.getTo());
        sendHtmlEmail(request);
    }
}
