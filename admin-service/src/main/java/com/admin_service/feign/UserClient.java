package com.admin_service.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.admin_service.config.FeignClientConfig;
import com.admin_service.dto.UserDto;

@FeignClient(name = "users-service",configuration = FeignClientConfig.class)
public interface UserClient {
   

    @GetMapping("/profiles/role/{role}")
    List<UserDto> getUsersByRole(@PathVariable("role") String role);
}


