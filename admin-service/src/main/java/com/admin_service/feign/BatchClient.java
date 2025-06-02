package com.admin_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.admin_service.config.FeignClientConfig;
import com.admin_service.dto.BatchDto;
import com.admin_service.dto.InstructorAssignmentDto;

@FeignClient(name = "batch-service",configuration = FeignClientConfig.class)
public interface BatchClient {
    @PostMapping("/batch/")
    ResponseEntity<?> createBatch(@RequestBody BatchDto batchDto);

    @PostMapping("/batch/{id}/assign-instructor")
    ResponseEntity<?> assignInstructor(@PathVariable Long id, @RequestBody InstructorAssignmentDto dto);
}
