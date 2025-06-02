package com.student_service.feign_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.student_service.dto.BatchDTO;  // Your batch DTO

@FeignClient(name = "batch-service")
public interface BatchClient {

    @GetMapping("/batches-for-student/{id}")
    BatchDTO getBatchById(@PathVariable Long id);
}
