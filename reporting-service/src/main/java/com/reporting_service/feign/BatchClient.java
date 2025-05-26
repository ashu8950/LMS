package com.reporting_service.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.reporting_service.dto.BatchDto;

@FeignClient(name = "batch-service")
public interface BatchClient {
    @GetMapping("/batch/{id}")
    BatchDto getBatch(@PathVariable("id") Long id);
}