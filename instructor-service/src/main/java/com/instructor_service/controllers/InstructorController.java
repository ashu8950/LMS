package com.instructor_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.instructor_service.dto.AssignmentRequest;
import com.instructor_service.dto.InstructorDTO;
import com.instructor_service.entity.Instructor;
import com.instructor_service.service.InstructorService;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Instructor create(@RequestBody InstructorDTO dto) {
        return service.createInstructor(dto);
    }

    @GetMapping
    public List<Instructor> all() {
        return service.listInstructors();
    }

    @GetMapping("/{id}")
    public Instructor get(@PathVariable Long id) {
        return service.getInstructor(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteInstructor(id);
    }

    @PostMapping("/assign")
    public void assign(@RequestBody AssignmentRequest req) {
        service.assignToBatch(req);
    }
}
