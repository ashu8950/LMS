package com.course_service.service;

import com.course_service.config.RequestHeaderContext;
import com.course_service.dto.CourseDTO;
import com.course_service.dto.NotificationRequest;
import com.course_service.entity.Course;
import com.course_service.feign.NotificationClient;
import com.course_service.mapper.CourseMapper;
import com.course_service.repository.CourseRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseService {

    private final RequestHeaderContext requestHeaderContext;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final NotificationClient notificationClient;

    public CourseService(CourseRepository courseRepository,
                         CourseMapper courseMapper,
                         NotificationClient notificationClient,
                         RequestHeaderContext requestHeaderContext) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.notificationClient = notificationClient;
        this.requestHeaderContext = requestHeaderContext;
    }

    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course = courseMapper.toEntity(courseDTO);
        Course savedCourse = courseRepository.save(course);
        CourseDTO savedDTO = courseMapper.toDTO(savedCourse);

        sendNotification(savedDTO, "New Course Created: ");

        return savedDTO;
    }

    // Get all courses
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

    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        // Update fields
        existingCourse.setTitle(courseDTO.getTitle());
        existingCourse.setDescription(courseDTO.getDescription());

        Course updatedCourse = courseRepository.save(existingCourse);
        CourseDTO updatedDTO = courseMapper.toDTO(updatedCourse);

        sendNotification(updatedDTO, "Course Updated: ");

        return updatedDTO;
    }

    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        courseRepository.delete(course);
        log.info("Deleted course with id: {}", id);
    }

    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return null;
        }
        return attrs.getRequest();
    }

    private void sendNotification(CourseDTO courseDTO, String subjectPrefix) {
        HttpServletRequest request = getCurrentHttpRequest();
        String userEmail = null;

        if (request != null) {
            userEmail = requestHeaderContext.getUserEmail(request);
        }

        if (userEmail == null || userEmail.isEmpty()) {
            userEmail = "admin@lms.com"; // fallback email if none found in request
        }

        NotificationRequest notification = new NotificationRequest();
        notification.setTo(userEmail);
        notification.setSubject(subjectPrefix + courseDTO.getTitle());
        notification.setBody("<p>The course titled <strong>" + courseDTO.getTitle() + "</strong> has been processed.</p>");
        notification.setType("schedule");
        try {
            notificationClient.sendNotification(notification);
            log.info("Notification sent to {}: {}", userEmail, notification.getSubject());
        } catch (Exception e) {
            log.error("Failed to send notification", e);
        }
    }

    

}
