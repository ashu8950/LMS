package com.user_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user_service.entity.UserProfile;
import com.user_service.repository.UserProfileRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")  
@RequiredArgsConstructor
public class StudentController {  

    private final UserProfileRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
