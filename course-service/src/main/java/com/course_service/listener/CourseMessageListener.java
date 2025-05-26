package com.course_service.listener;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.course_service.dto.CourseDTO;

import lombok.extern.slf4j.Slf4j;



@Service
@Slf4j
public class CourseMessageListener {

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void receiveMessage(CourseDTO courseDTO) {
        log.info("Received Course message from queue: {}", courseDTO);
        // Handle message if needed
    }
}
