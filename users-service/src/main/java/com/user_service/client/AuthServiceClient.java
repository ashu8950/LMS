package com.user_service.client;

import com.user_service.dto.AuthUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {

    @GetMapping("/users/{id}")
    AuthUserDTO getUserById(@PathVariable("id") Long id);

    @GetMapping("/users")
    List<AuthUserDTO> getAllUsers();

    @GetMapping("/users/role/{role}")
    List<AuthUserDTO> getUsersByRole(@PathVariable("role") String role);
}
