package com.batch_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course-service")
public interface CourseClient {
	
	@GetMapping("/courses-check/{id}")
	Boolean existById(@PathVariable("id")Long id);

}
