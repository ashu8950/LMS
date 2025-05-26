package com.course_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.course_service.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}

