package com.reporting_service.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.reporting_service.dto.StudentDto;

@FeignClient(name = "user-service")  
public interface StudentClient {
    @GetMapping("/student/{id}")
    StudentDto getStudent(@PathVariable("id") Long id);
}