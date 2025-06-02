package com.admin_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.admin_service.config.FeignClientConfig;
import com.admin_service.dto.CourseDto;

@FeignClient(name = "course-service",configuration = FeignClientConfig.class)
public interface CourseClient {
    @PostMapping("/course/")
    ResponseEntity<?> createCourse(@RequestBody CourseDto dto);
}
