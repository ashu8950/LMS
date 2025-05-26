package com.instructor_service.service;



import java.util.List;

import com.instructor_service.dto.AssignmentRequest;
import com.instructor_service.dto.InstructorDTO;
import com.instructor_service.entity.Instructor;

public interface InstructorService {
    Instructor createInstructor(InstructorDTO dto);
    List<Instructor> listInstructors();
    Instructor getInstructor(Long id);
    void deleteInstructor(Long id);
    void assignToBatch(AssignmentRequest req);
}