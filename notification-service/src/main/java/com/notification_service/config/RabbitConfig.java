package com.notification_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // Exchange
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";

    // Queues
    public static final String SCHEDULE_QUEUE = "schedule.queue";
    public static final String FEEDBACK_QUEUE = "feedback.queue";
    public static final String PAYMENT_QUEUE = "payment.queue";
    public static final String OTP_QUEUE = "otp.queue";

    public static final String REGISTER_QUEUE = "register.queue";
    public static final String PROFILE_UPDATE_QUEUE = "profile_update.queue";
    public static final String ENROLLMENT_CONFIRMATION_QUEUE = "enrollment_confirmation.queue";
    public static final String FEEDBACK_RECEIVED_QUEUE = "feedback_received.queue";

    // Routing Keys
    public static final String SCHEDULE_ROUTING_KEY = "notification.schedule";
    public static final String FEEDBACK_ROUTING_KEY = "notification.feedback";
    public static final String PAYMENT_ROUTING_KEY = "notification.payment";
    public static final String OTP_ROUTING_KEY = "notification.otp";

    public static final String REGISTER_ROUTING_KEY = "notification.register";
    public static final String PROFILE_UPDATE_ROUTING_KEY = "notification.profile_update";
    public static final String ENROLLMENT_CONFIRMATION_ROUTING_KEY = "notification.enrollment_confirmation";
    public static final String FEEDBACK_RECEIVED_ROUTING_KEY = "notification.feedback_received";

    // Exchange Bean
    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(NOTIFICATION_EXCHANGE);
    }

    // Queue Beans
    @Bean
    public Queue scheduleQueue() {
        return new Queue(SCHEDULE_QUEUE, true);
    }

    @Bean
    public Queue feedbackQueue() {
        return new Queue(FEEDBACK_QUEUE, true);
    }

    @Bean
    public Queue paymentQueue() {
        return new Queue(PAYMENT_QUEUE, true);
    }

    @Bean
    public Queue otpQueue() {
        return new Queue(OTP_QUEUE, true);
    }

    @Bean
    public Queue registerQueue() {
        return new Queue(REGISTER_QUEUE, true);
    }

    @Bean
    public Queue profileUpdateQueue() {
        return new Queue(PROFILE_UPDATE_QUEUE, true);
    }

    @Bean
    public Queue enrollmentConfirmationQueue() {
        return new Queue(ENROLLMENT_CONFIRMATION_QUEUE, true);
    }

    @Bean
    public Queue feedbackReceivedQueue() {
        return new Queue(FEEDBACK_RECEIVED_QUEUE, true);
    }

    // Binding Beans
    @Bean
    public Binding scheduleBinding() {
        return BindingBuilder.bind(scheduleQueue())
                .to(notificationExchange())
                .with(SCHEDULE_ROUTING_KEY);
    }

    @Bean
    public Binding feedbackBinding() {
        return BindingBuilder.bind(feedbackQueue())
                .to(notificationExchange())
                .with(FEEDBACK_ROUTING_KEY);
    }

    @Bean
    public Binding paymentBinding() {
        return BindingBuilder.bind(paymentQueue())
                .to(notificationExchange())
                .with(PAYMENT_ROUTING_KEY);
    }

    @Bean
    public Binding otpBinding() {
        return BindingBuilder.bind(otpQueue())
                .to(notificationExchange())
                .with(OTP_ROUTING_KEY);
    }

    @Bean
    public Binding registerBinding() {
        return BindingBuilder.bind(registerQueue())
                .to(notificationExchange())
                .with(REGISTER_ROUTING_KEY);
    }

    @Bean
    public Binding profileUpdateBinding() {
        return BindingBuilder.bind(profileUpdateQueue())
                .to(notificationExchange())
                .with(PROFILE_UPDATE_ROUTING_KEY);
    }

    @Bean
    public Binding enrollmentConfirmationBinding() {
        return BindingBuilder.bind(enrollmentConfirmationQueue())
                .to(notificationExchange())
                .with(ENROLLMENT_CONFIRMATION_ROUTING_KEY);
    }

    @Bean
    public Binding feedbackReceivedBinding() {
        return BindingBuilder.bind(feedbackReceivedQueue())
                .to(notificationExchange())
                .with(FEEDBACK_RECEIVED_ROUTING_KEY);
    }

    // Message Converter
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Listener Container Factory with JSON converter
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(jackson2JsonMessageConverter());
        return factory;
    }
}
