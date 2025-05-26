package com.instructor_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.instructor_service.entity.Instructor;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    boolean existsByEmail(String email);
}
