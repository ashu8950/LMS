package com.user_service.service.impl;

import com.user_service.client.AuthServiceClient;
import com.user_service.dto.AuthUserDTO;
import com.user_service.dto.UserProfileDTO;
import com.user_service.entity.UserProfile;
import com.user_service.mapper.UserProfileMapper;
import com.user_service.repository.UserProfileRepository;
import com.user_service.service.UserProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final AuthServiceClient authServiceClient;
    private final Path rootLocation = Paths.get("C:/Users/DELL/Desktop/filePath");

    // Create new user profile
    @Override
    public UserProfileDTO createUserProfile(UserProfileDTO dto) {
        if (dto == null || dto.getUserId() == null) {
            throw new IllegalArgumentException("UserProfileDTO or userId cannot be null");
        }

        AuthUserDTO authUser = authServiceClient.getUserById(dto.getUserId());
        if (authUser == null) {
            throw new RuntimeException("User not found in auth-service with id " + dto.getUserId());
        }

        UserProfile userProfile = UserProfileMapper.toEntity(dto, authUser);
        userProfile = userProfileRepository.save(userProfile);

        return UserProfileMapper.toDTO(userProfile, authUser);
    }

    // Update existing user profile
    @Override
    public UserProfileDTO updateUserProfile(UserProfileDTO dto) {
        if (dto == null || dto.getUserId() == null) {
            throw new IllegalArgumentException("UserProfileDTO or userId cannot be null");
        }

        UserProfile existing = userProfileRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User profile not found with id " + dto.getUserId()));

        AuthUserDTO authUser = authServiceClient.getUserById(dto.getUserId());
        if (authUser == null) {
            throw new RuntimeException("User not found in auth-service with id " + dto.getUserId());
        }

        UserProfileMapper.updateEntity(existing, dto, authUser);
        userProfileRepository.save(existing);

        return UserProfileMapper.toDTO(existing, authUser);
    }

    // Get user profile by user ID
    @Override
    public Optional<UserProfileDTO> getUserProfile(Long userId) {
        if (userId == null) {
            return Optional.empty();
        }

        Optional<UserProfile> profileOpt = userProfileRepository.findById(userId);
        if (profileOpt.isEmpty()) {
            return Optional.empty();
        }

        AuthUserDTO authUser = authServiceClient.getUserById(userId);
        if (authUser == null) {
            return Optional.empty();
        }

        return Optional.of(UserProfileMapper.toDTO(profileOpt.get(), authUser));
    }

    // Delete user profile by ID
    @Override
    public void deleteUserProfile(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        userProfileRepository.deleteById(userId);
    }

    // Get all profiles, combine local + auth, unique by ID
    @Override
    public List<UserProfileDTO> getAllUserProfiles() {
        Map<Long, UserProfileDTO> profileMap = new HashMap<>();

        // Load local profiles
        List<UserProfile> localProfiles = userProfileRepository.findAll();
        for (UserProfile profile : localProfiles) {
            AuthUserDTO authUser = authServiceClient.getUserById(profile.getAuthUserId());
            if (authUser != null) {
                profileMap.put(authUser.getId(), UserProfileMapper.toDTO(profile, authUser));
            }
        }

        // Load all auth-users and merge if not present
        List<AuthUserDTO> authUsers = authServiceClient.getAllUsers();
        for (AuthUserDTO authUser : authUsers) {
            profileMap.computeIfAbsent(authUser.getId(),
                    id -> UserProfileMapper.toDTO(null, authUser));
        }

        return new ArrayList<>(profileMap.values());
    }

    // Get profiles by role, local first, merge auth, unique by ID
    @Override
    public List<UserProfileDTO> getProfilesByRole(String role) {
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role must be provided");
        }

        String upperRole = role.toUpperCase();
        Map<Long, UserProfileDTO> profileMap = new HashMap<>();

        // Load local profiles by role
        List<UserProfile> localProfiles = userProfileRepository.findByRole(upperRole);
        for (UserProfile profile : localProfiles) {
            AuthUserDTO authUser = authServiceClient.getUserById(profile.getAuthUserId());
            if (authUser != null) {
                profileMap.put(authUser.getId(), UserProfileMapper.toDTO(profile, authUser));
            }
        }

        // Load auth users by role and add if not already present
        List<AuthUserDTO> authUsers = authServiceClient.getUsersByRole(upperRole);
        for (AuthUserDTO authUser : authUsers) {
            profileMap.computeIfAbsent(authUser.getId(),
                    id -> UserProfileMapper.toDTO(null, authUser));
        }

        return new ArrayList<>(profileMap.values());
    }

    // Store profile photo on disk and update DB
    @Override
    public String storePhoto(Long userId, MultipartFile file) throws IOException {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }

        Path photosDir = rootLocation.resolve("profile-photos");
        Files.createDirectories(photosDir);

        String originalFilename = Path.of(file.getOriginalFilename()).getFileName().toString();
        String extension = "";

        int extIndex = originalFilename.lastIndexOf('.');
        if (extIndex >= 0) {
            extension = originalFilename.substring(extIndex);
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String filename = String.format("profile_%d_%s%s", userId, timestamp, extension);

        Path destinationFile = photosDir.resolve(filename).normalize().toAbsolutePath();

        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        }

        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        String relativePath = "/uploads/profile-photos/" + filename;
        profile.setProfileImageUrl(relativePath);
        userProfileRepository.save(profile);

        return relativePath;
    }
}
