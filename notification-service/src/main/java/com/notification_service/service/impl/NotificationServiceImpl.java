package com.notification_service.service.impl;

import com.notification_service.config.RabbitConfig;
import com.notification_service.dto.NotificationRequest;
import com.notification_service.service.NotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;

    @Override
    public void sendHtmlEmail(NotificationRequest req) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, "UTF-8");

            helper.setTo(req.getTo());
            helper.setSubject(req.getSubject());
            helper.setText(req.getBody(), true);

            mailSender.send(msg);
            log.info("Email sent to {}", req.getTo());
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", req.getTo(), e.getMessage());
        }
    }

    @RabbitListener(queues = RabbitConfig.SCHEDULE_QUEUE)
    public void handleScheduleEvent(NotificationRequest request) {
        log.info("Received schedule.new event for: {}", request.getTo());
        sendHtmlEmail(request);
    }

    @RabbitListener(queues = RabbitConfig.FEEDBACK_QUEUE)
    public void handleFeedbackEvent(NotificationRequest request) {
        log.info("Received feedback.submitted event for: {}", request.getTo());
        sendHtmlEmail(request);
    }

//    @RabbitListener(queues = RabbitConfig.PAYMENT_QUEUE)
//    public void handlePaymentSuccessEvent(NotificationRequest request) {
//        log.info("Received payment.success event for: {}", request.getTo());
//        sendHtmlEmail(request);
//    }
//
//    @RabbitListener(queues = RabbitConfig.OTP_QUEUE)
//    public void handleOtpEvent(NotificationRequest request) {
//        log.info("Received otp.generated event for: {}", request.getTo());
//        sendHtmlEmail(request);
//    }
}
