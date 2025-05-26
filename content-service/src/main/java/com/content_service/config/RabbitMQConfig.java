package com.content_service.config;



import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.queue}")
    private String queue;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingKey;

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue contentQueue() {
        return new Queue(queue);
    }

    @Bean
    public Binding binding(Queue contentQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(contentQueue).to(topicExchange).with(routingKey);
    }
}

