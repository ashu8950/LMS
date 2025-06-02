package com.student_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.student_service.dto.CourseDTO;
import com.student_service.dto.EnrollmentRequest;
import com.student_service.dto.FeedbackDto;
import com.student_service.dto.StudentDTO;
import com.student_service.entity.Student;
import com.student_service.service.StudentService;
import com.student_service.util.RequestHeaderContext;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final RequestHeaderContext requestHeaderContext;

    @PostMapping("/register")
    public ResponseEntity<Student> register(
            @RequestBody StudentDTO dto,
            HttpServletRequest request) throws Exception {

        if (!isAdminOrInstructorOrSelf(request, dto.getUserId())) {
            return ResponseEntity.status(403).build();
        }

        Student created = studentService.registerStudent(dto);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/{id}/enroll")
    public ResponseEntity<Void> enroll(
            @PathVariable Long id,
            @RequestBody EnrollmentRequest req,
            HttpServletRequest request) {

        if (!isAdminOrInstructorOrSelf(request, id)) {
            return ResponseEntity.status(403).build();
        }

        studentService.enrollStudent(id, req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/feedback")
    public ResponseEntity<Void> submitFeedback(
            @PathVariable Long id,
            @RequestBody FeedbackDto dto,
            HttpServletRequest request) {

        if (!isAdminOrInstructorOrSelf(request, id)) {
            return ResponseEntity.status(403).build();
        }

        studentService.sendFeedback(id, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<List<CourseDTO>> getEnrolledCourses(
            @PathVariable Long id,
            HttpServletRequest request) {

        if (!isAdminOrInstructorOrSelf(request, id)) {
            return ResponseEntity.status(403).build();
        }

        List<CourseDTO> courses = studentService.getEnrolledCourses(id);
        return ResponseEntity.ok(courses);
    }

    private boolean isAdminOrInstructorOrSelf(HttpServletRequest request, Long targetUserId) {
        String currentUserId = requestHeaderContext.getUserId(request);
        return requestHeaderContext.hasRole(request, "ADMIN")
            || requestHeaderContext.hasRole(request, "INSTRUCTOR")
            || targetUserId.toString().equals(currentUserId);
    }
}
