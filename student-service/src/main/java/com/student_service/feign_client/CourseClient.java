package com.student_service.feign_client;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.student_service.dto.CourseDTO;

@FeignClient(name = "course-service")
public interface CourseClient {
    @GetMapping("/courses-for-student/{id}")
    CourseDTO getCourseById(@PathVariable Long id);
}


