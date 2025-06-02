package com.admin_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.admin_service.config.FeignClientConfig;

@FeignClient(name = "reporting-service",configuration = FeignClientConfig.class)
public interface ReportingClient {
    @GetMapping("/report/student/{id}")
    ResponseEntity<?> getStudentReport(@PathVariable Long id);

    @GetMapping("/report/batch/{id}")
    ResponseEntity<?> getBatchReport(@PathVariable Long id);
}
