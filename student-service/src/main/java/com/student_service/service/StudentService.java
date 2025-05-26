package com.student_service.service;

import java.util.List;

import com.student_service.dto.CourseDTO;
import com.student_service.dto.EnrollmentRequest;
import com.student_service.dto.FeedbackDto;
import com.student_service.dto.StudentDTO;
import com.student_service.entity.Student;


public interface StudentService {
    Student registerStudent(StudentDTO dto);
    void enrollStudent(Long studentId, EnrollmentRequest req);
    List<CourseDTO> getEnrolledCourses(Long studentId);
    void sendFeedback(Long studentId, FeedbackDto feedback);
}
