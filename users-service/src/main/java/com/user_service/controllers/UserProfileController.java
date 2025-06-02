package com.user_service.controllers;

import com.user_service.client.AuthServiceClient;
import com.user_service.dto.AuthUserDTO;
import com.user_service.dto.UserProfileDTO;
import com.user_service.service.UserProfileService;
import com.user_service.util.RequestHeaderContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final AuthServiceClient authServiceClient;
    private final RequestHeaderContext requestHeaderContext;

    
    private boolean isOwnerOrRole(HttpServletRequest request, Long userId, String email, String role) {
        String requesterId = requestHeaderContext.getUserId(request);
        String requesterEmail = requestHeaderContext.getUserEmail(request);
        boolean hasRole = requestHeaderContext.hasRole(request, role);
        boolean isOwnerById = userId != null && String.valueOf(userId).equals(requesterId);
        boolean isOwnerByEmail = email != null && email.equalsIgnoreCase(requesterEmail);
        return hasRole || isOwnerById || isOwnerByEmail;
    }

    @PostMapping
    public ResponseEntity<UserProfileDTO> createProfile(HttpServletRequest request,
                                                        @Valid @RequestBody UserProfileDTO dto) {
        // Allow if ADMIN or the user themselves by ID or email
        if (!isOwnerOrRole(request, dto.getUserId(), dto.getEmail(), "ADMIN")) {
            return ResponseEntity.status(403).build();
        }
        // Optionally validate existence in auth-service
        AuthUserDTO user = authServiceClient.getUserById(dto.getUserId());
        if (user == null || !user.getEmail().equalsIgnoreCase(dto.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        UserProfileDTO createdProfile = userProfileService.createUserProfile(dto);
        return ResponseEntity.status(201).body(createdProfile);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> updateProfile(HttpServletRequest request,
                                                        @PathVariable Long userId,
                                                        @Valid @RequestBody UserProfileDTO dto) {
        // Allow if ADMIN or the user themselves
        if (!isOwnerOrRole(request, userId, dto.getEmail(), "ADMIN")) {
            return ResponseEntity.status(403).build();
        }
        dto.setUserId(userId);
        UserProfileDTO updatedProfile = userProfileService.updateUserProfile(dto);
        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> getProfile(HttpServletRequest request,
                                                     @PathVariable Long userId) {
        // Allow if ADMIN, INSTRUCTOR, or the user by ID
        boolean allowed = requestHeaderContext.hasRole(request, "ADMIN") ||
                          requestHeaderContext.hasRole(request, "INSTRUCTOR") ||
                          userId.equals(Long.valueOf(requestHeaderContext.getUserId(request)));
        if (!allowed) {
            return ResponseEntity.status(403).build();
        }
        Optional<UserProfileDTO> profile = userProfileService.getUserProfile(userId);
        return profile.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteProfile(HttpServletRequest request,
                                              @PathVariable Long userId) {
        if (!isOwnerOrRole(request, userId, null, "ADMIN")) {
            return ResponseEntity.status(403).build();
        }
        userProfileService.deleteUserProfile(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserProfileDTO>> getAllProfiles(HttpServletRequest request) {
        if (!requestHeaderContext.hasRole(request, "ADMIN")) {
            return ResponseEntity.status(403).build();
        }
        List<UserProfileDTO> profiles = userProfileService.getAllUserProfiles();
        return ResponseEntity.ok(profiles);
    }

    @PostMapping("/{userId}/photo")
    public ResponseEntity<String> uploadPhoto(HttpServletRequest request,
                                              @PathVariable Long userId,
                                              @RequestParam("file") MultipartFile file) {
        if (!isOwnerOrRole(request, userId, null, "ADMIN")) {
            return ResponseEntity.status(403).build();
        }
        try {
            String filename = userProfileService.storePhoto(userId, file);
            return ResponseEntity.ok("Photo uploaded successfully: " + filename);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error uploading photo: " + e.getMessage());
        }
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserProfileDTO>> getProfilesByRole(HttpServletRequest request,
                                                                  @PathVariable String role) {
        // only admins
        if (!requestHeaderContext.hasRole(request, "ADMIN")) {
            return ResponseEntity.status(403).build();
        }
        List<UserProfileDTO> profiles = userProfileService.getProfilesByRole(role);
        return ResponseEntity.ok(profiles);
    }
}
