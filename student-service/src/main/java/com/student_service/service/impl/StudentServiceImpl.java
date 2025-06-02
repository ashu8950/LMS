package com.student_service.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.student_service.dto.BatchDTO;
import com.student_service.dto.CourseDTO;
import com.student_service.dto.EnrollmentRequest;
import com.student_service.dto.FeedbackDto;
import com.student_service.dto.NotificationRequest;
import com.student_service.dto.StudentDTO;
import com.student_service.dto.UserDTO;
import com.student_service.entity.Enrollment;
import com.student_service.entity.Student;
import com.student_service.exception.StudentNotFoundException;
import com.student_service.exception.UserRoleInvalidException;
import com.student_service.feign_client.BatchClient;
import com.student_service.feign_client.CourseClient;
import com.student_service.feign_client.NotificationClient;
import com.student_service.feign_client.UserClient;
import com.student_service.repository.EnrollmentRepository;
import com.student_service.repository.StudentRepository;
import com.student_service.service.StudentService;
import com.student_service.util.EmailTemplateBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseClient courseClient;
    private final BatchClient batchClient;
    private final UserClient userClient;
    private final NotificationClient notificationClient;

    @Override
    @Transactional
    public Student registerStudent(StudentDTO dto) {
        boolean isNew = !studentRepository.existsById(dto.getUserId());
        Student student;

        UserDTO user = userClient.getUserById(dto.getUserId());
        if (user == null) {
            throw new StudentNotFoundException("No user found with ID: " + dto.getUserId());
        }
        if (!"STUDENT".equalsIgnoreCase(user.getRole())) {
            throw new UserRoleInvalidException("User ID " + dto.getUserId() + " does not have STUDENT role.");
        }

        if (isNew) {
            if (!StringUtils.hasText(dto.getStudentCode())) {
                throw new IllegalArgumentException("studentCode is required for new students");
            }
            student = new Student();
            student.setUserId(dto.getUserId());
            student.setFirstName(user.getFirstName());
            student.setLastName(user.getLastName());
            student.setEmail(user.getEmail());
            student.setPhone(user.getPhone());
            student.setJoinedDate(LocalDate.now());
            student.setStudentCode(dto.getStudentCode().trim());
        } else {
            student = studentRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + dto.getUserId()));
            if (!StringUtils.hasText(student.getEmail())) {
                student.setEmail(user.getEmail());
            }
        }

        student.setStatus(dto.getStatus());
        Student saved = studentRepository.save(student);

        String to = saved.getEmail();
        if (!StringUtils.hasText(to)) {
            log.warn("Student registered but email is null. Skipping notification. Student ID: {}", saved.getUserId());
            return saved;
        }

        String subject;
        String body;

        if (isNew) {
            subject = "Welcome to LMS";
            body = EmailTemplateBuilder.buildRegisterBody(saved.getFirstName(), saved.getJoinedDate());
            log.info("Sending welcome email to: {}", to);
            notificationClient.sendEmail(new NotificationRequest("REGISTER", to, subject, body));
        } else {
            subject = "Your profile was updated";
            body = EmailTemplateBuilder.buildUpdateBody(saved.getFirstName(), LocalDate.now());
            log.info("Sending profile update email to: {}", to);
            notificationClient.sendEmail(new NotificationRequest("PROFILE_UPDATE", to, subject, body));
        }

        return saved;
    }

    @Override
    @Transactional
    public void enrollStudent(Long studentId, EnrollmentRequest req) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + studentId));

        // Validate course existence
        CourseDTO course = courseClient.getCourseById(req.getCourseId());
        if (course == null) {
            throw new IllegalArgumentException("Course not found with ID: " + req.getCourseId());
        }

        // Validate batch existence
        BatchDTO batch = batchClient.getBatchById(req.getBatchId());
        if (batch == null) {
            throw new IllegalArgumentException("Batch not found with ID: " + req.getBatchId());
        }

        // Check batch belongs to the course
        if (!req.getCourseId().equals(batch.getId())) {
            throw new IllegalArgumentException("Batch ID " + req.getBatchId() + " does not belong to Course ID " + req.getCourseId());
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourseId(req.getCourseId());
        enrollment.setBatchId(req.getBatchId());
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus("ACTIVE");

        enrollmentRepository.save(enrollment);

        String to = student.getEmail();
        if (StringUtils.hasText(to)) {
            String subject = "Enrollment Confirmation";

            // Build email body with course title and batch name
            String body = EmailTemplateBuilder.buildEnrollmentBodyWithNames(
                student.getFirstName(),
                course.getId(), course.getTitle(),
                batch.getId(), batch.getName()
            );

            notificationClient.sendEmail(new NotificationRequest("ENROLLMENT_CONFIRMATION", to, subject, body));
            log.info("Enrollment confirmation email sent to {} for student ID {}", to, studentId);
        } else {
            log.warn("Enrollment email skipped due to missing student email for ID {}", studentId);
        }
    }

    @Override
    public List<CourseDTO> getEnrolledCourses(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException("Student not found with ID: " + studentId);
        }
        return enrollmentRepository.findByStudentUserId(studentId).stream()
                .map(enrollment -> courseClient.getCourseById(enrollment.getCourseId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void sendFeedback(Long studentId, FeedbackDto feedback) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + studentId));

        feedback.setStudentId(studentId);
        feedback.setSubmittedDate(LocalDate.now());

        String to = student.getEmail();
        if (StringUtils.hasText(to)) {
            String subject = "Feedback Received";
            String body = EmailTemplateBuilder.buildFeedbackBody(
                student.getFirstName(),
                feedback.getSubmittedDate()
            );
            notificationClient.sendEmail(new NotificationRequest("FEEDBACK_RECEIVED", to, subject, body));
        } else {
            log.warn("Feedback email skipped due to missing email for student ID {}", studentId);
        }
    }
}
