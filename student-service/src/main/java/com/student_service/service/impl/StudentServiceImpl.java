package com.student_service.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.student_service.feign_client.CourseClient;
import com.student_service.feign_client.NotificationClient;
import com.student_service.feign_client.UserClient;
import com.student_service.repository.EnrollmentRepository;
import com.student_service.repository.StudentRepository;
import com.student_service.service.StudentService;
import com.student_service.util.EmailTemplateBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseClient courseClient;
    private final UserClient userClient;
    private final NotificationClient notificationClient;

    @Override
    public Student registerStudent(StudentDTO dto) {
        UserDTO user = userClient.getUserById(dto.getUserId());
        if (user == null) {
            throw new StudentNotFoundException("No user found with ID: " + dto.getUserId());
        }
        if (!"STUDENT".equalsIgnoreCase(user.getRole())) {
            throw new UserRoleInvalidException(
                "User with ID " + dto.getUserId() + " does not have the STUDENT role."
            );
        }

        Student student = new Student();
        student.setUserId(user.getId());
        student.setFirstName(user.getFirstName());
        student.setLastName(user.getLastName());
        student.setEmail(user.getEmail());
        student.setPhone(user.getPhone());
        student.setJoinedDate(dto.getJoinedDate());
        student.setStatus(dto.getStatus());

        Student saved = studentRepository.save(student);

        // Send welcome notification
        NotificationRequest welcomeNotification = new NotificationRequest(
            user.getEmail(),
            "Welcome to LMS",
            EmailTemplateBuilder.buildRegisterBody(user.getFirstName(), saved.getJoinedDate())
        );
        notificationClient.sendEmail(welcomeNotification);

        return saved;
    }

    @Override
    @Transactional
    public void enrollStudent(Long studentId, EnrollmentRequest req) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + studentId));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourseId(req.getCourseId());
        enrollment.setBatchId(req.getBatchId());
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus("ACTIVE");
        enrollmentRepository.save(enrollment);

        // Send enrollment confirmation via Notification Service
        NotificationRequest enrollNotification = new NotificationRequest(
            student.getEmail(),
            "Enrollment Confirmation",
            EmailTemplateBuilder.buildEnrollmentBody(
                student.getFirstName(),
                req.getCourseId(),
                req.getBatchId()
            )
        );
        notificationClient.sendEmail(enrollNotification);
    }

    @Override
    public List<CourseDTO> getEnrolledCourses(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException("Student not found with ID: " + studentId);
        }

        return enrollmentRepository.findByStudentId(studentId)
            .stream()
            .map(enrollment -> courseClient.getCourseById(enrollment.getCourseId()))
            .collect(Collectors.toList());
    }

    @Override
    public void sendFeedback(Long studentId, FeedbackDto feedback) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + studentId));

        feedback.setStudentId(studentId);
        feedback.setSubmittedDate(LocalDate.now());

        // Send feedback notification using NotificationClient
        NotificationRequest feedbackNotification = new NotificationRequest(
            student.getEmail(),
            "Feedback Received",
            EmailTemplateBuilder.buildFeedbackBody(student.getFirstName(), feedback.getSubmittedDate())
        );
        notificationClient.sendEmail(feedbackNotification);
    }
}
