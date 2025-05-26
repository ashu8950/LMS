package com.course_service.service;

import com.course_service.dto.CourseDTO;
import com.course_service.dto.NotificationRequest;
import com.course_service.entity.Course;
import com.course_service.feign.NotificationClient;
import com.course_service.mapper.CourseMapper;
import com.course_service.repository.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final NotificationClient notificationClient;

    public CourseService(CourseRepository courseRepository,
                         CourseMapper courseMapper,
                         NotificationClient notificationClient) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.notificationClient = notificationClient;
    }

    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course = courseMapper.toEntity(courseDTO);
        Course savedCourse = courseRepository.save(course);

        CourseDTO savedDTO = courseMapper.toDTO(savedCourse);

        // Send notification
        NotificationRequest notification = new NotificationRequest();
        notification.setTo("admin@lms.com"); // Or dynamic email
        notification.setSubject("New Course Created: " + savedDTO.getTitle());
        notification.setBody("<p>A new course titled <strong>" + savedDTO.getTitle() + "</strong> has been created.</p>");

        try {
            notificationClient.sendNotification(notification);
            log.info("Notification sent for new course: {}", savedDTO.getTitle());
        } catch (Exception e) {
            log.error("Failed to send notification for course creation", e);
        }

        return savedDTO;
    }

    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(courseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        return courseMapper.toDTO(course);
    }
}
