package com.user_service.service;

import com.user_service.dto.UserProfileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserProfileService {

    UserProfileDTO createUserProfile(UserProfileDTO dto);

    UserProfileDTO updateUserProfile(UserProfileDTO dto);

    Optional<UserProfileDTO> getUserProfile(Long userId);

    void deleteUserProfile(Long userId);

    List<UserProfileDTO> getAllUserProfiles();

    List<UserProfileDTO> getProfilesByRole(String role);

    String storePhoto(Long userId, MultipartFile file) throws IOException;
}
