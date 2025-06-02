package com.course_service.controller;

import com.course_service.dto.CourseDTO;
import com.course_service.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses-for-student")
public class StudentController {

    private final CourseService courseService;

    @Autowired
    public StudentController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
        try {
            CourseDTO course = courseService.getCourseById(id);
            return ResponseEntity.ok(course);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
