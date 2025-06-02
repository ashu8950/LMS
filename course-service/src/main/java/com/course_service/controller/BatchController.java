package com.course_service.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.course_service.repository.CourseRepository;
@RestController
@RequestMapping("/courses-check")
public class BatchController {

    @Autowired
    private CourseRepository repository;

    @GetMapping("{id}")
    public ResponseEntity<Boolean> existById(@PathVariable("id") Long id) {
        boolean exists = repository.existsById(id);
        return ResponseEntity.ok(exists);
    }
}
