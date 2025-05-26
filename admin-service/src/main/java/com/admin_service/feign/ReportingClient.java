package com.admin_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reporting-service")
public interface ReportingClient {
    @GetMapping("/report/student/{id}")
    ResponseEntity<?> getStudentReport(@PathVariable Long id);

    @GetMapping("/report/batch/{id}")
    ResponseEntity<?> getBatchReport(@PathVariable Long id);
}
