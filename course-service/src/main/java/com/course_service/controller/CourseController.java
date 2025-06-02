package com.course_service.controller;

import com.course_service.config.RequestHeaderContext;
import com.course_service.dto.CourseDTO;
import com.course_service.service.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final RequestHeaderContext requestHeaderContext;

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO,
                                                  HttpServletRequest request) {
        if (!isAdminOrInstructor(request)) {
            return ResponseEntity.status(403).build();
        }
        CourseDTO created = courseService.createCourse(courseDTO);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses(HttpServletRequest request) {
        if (!isAdminOrInstructor(request)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id,
                                                   HttpServletRequest request) {
        if (!isAdminOrInstructor(request)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long id,
                                                  @RequestBody CourseDTO courseDTO,
                                                  HttpServletRequest request) {
        if (!isAdminOrInstructor(request)) {
            return ResponseEntity.status(403).build();
        }
        CourseDTO updated = courseService.updateCourse(id, courseDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id,
                                             HttpServletRequest request) {
        if (!isAdminOrInstructor(request)) {
            return ResponseEntity.status(403).build();
        }
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    private boolean isAdminOrInstructor(HttpServletRequest request) {
        return requestHeaderContext.hasRole(request, "ADMIN") ||
               requestHeaderContext.hasRole(request, "INSTRUCTOR");
    }
}
