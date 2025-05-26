package com.notification_service.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "lms.exchange";

    public static final String SCHEDULE_QUEUE = "lms.schedule.queue";
    public static final String FEEDBACK_QUEUE = "lms.feedback.queue";
    public static final String PAYMENT_QUEUE = "lms.payment.queue";
    public static final String OTP_QUEUE = "lms.otp.queue";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    // === SCHEDULE QUEUE ===
    @Bean
    public Queue scheduleQueue() {
        return new Queue(SCHEDULE_QUEUE);
    }

    @Bean
    public Binding bindScheduleQueue() {
        return BindingBuilder
                .bind(scheduleQueue())
                .to(exchange())
                .with("schedule.*");
    }

    // === FEEDBACK QUEUE ===
    @Bean
    public Queue feedbackQueue() {
        return new Queue(FEEDBACK_QUEUE);
    }

    @Bean
    public Binding bindFeedbackQueue() {
        return BindingBuilder
                .bind(feedbackQueue())
                .to(exchange())
                .with("feedback.*");
    }

//    // === PAYMENT QUEUE ===
//    @Bean
//    public Queue paymentQueue() {
//        return new Queue(PAYMENT_QUEUE);
//    }
//
//    @Bean
//    public Binding bindPaymentQueue() {
//        return BindingBuilder
//                .bind(paymentQueue())
//                .to(exchange())
//                .with("payment.*");
//    }
//
//    // === OTP QUEUE ===
//    @Bean
//    public Queue otpQueue() {
//        return new Queue(OTP_QUEUE);
//    }
//
//    @Bean
//    public Binding bindOtpQueue() {
//        return BindingBuilder
//                .bind(otpQueue())
//                .to(exchange())
//                .with("otp.*");
//    }
}
