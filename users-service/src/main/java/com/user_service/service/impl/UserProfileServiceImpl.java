package com.user_service.service.impl;

import com.user_service.client.AuthServiceClient;
import com.user_service.dto.AuthUserDTO;
import com.user_service.dto.UserProfileDTO;
import com.user_service.entity.UserProfile;
import com.user_service.mapper.UserProfileMapper;
import com.user_service.repository.UserProfileRepository;
import com.user_service.service.UserProfileService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final AuthServiceClient authServiceClient;
    private final Path rootLocation;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository,
                                  AuthServiceClient authServiceClient,
                                  @Value("${user-service.file-storage.location}") String storageLocation) {
        this.userProfileRepository = userProfileRepository;
        this.authServiceClient = authServiceClient;
        this.rootLocation = Paths.get(storageLocation);
    }

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

    @Override
    public Optional<UserProfileDTO> getUserProfile(Long userId) {
        if (userId == null) return Optional.empty();
        Optional<UserProfile> profileOpt = userProfileRepository.findById(userId);
        if (profileOpt.isEmpty()) return Optional.empty();
        AuthUserDTO authUser = authServiceClient.getUserById(userId);
        if (authUser == null) return Optional.empty();
        return Optional.of(UserProfileMapper.toDTO(profileOpt.get(), authUser));
    }

    @Override
    public void deleteUserProfile(Long userId) {
        if (userId == null) throw new IllegalArgumentException("UserId cannot be null");
        userProfileRepository.deleteById(userId);
    }

    @Override
    public List<UserProfileDTO> getAllUserProfiles() {
        Map<Long, UserProfileDTO> profileMap = new HashMap<>();
        // load all local
        for (UserProfile profile : userProfileRepository.findAll()) {
            AuthUserDTO authUser = authServiceClient.getUserById(profile.getAuthUserId());
            if (authUser != null) {
                profileMap.put(authUser.getId(),
                        UserProfileMapper.toDTO(profile, authUser));
            }
        }
        // fill in any auth-only
        for (AuthUserDTO authUser : authServiceClient.getAllUsers()) {
            profileMap.computeIfAbsent(authUser.getId(),
                    id -> UserProfileMapper.toDTO(null, authUser));
        }
        return new ArrayList<>(profileMap.values());
    }

    @Override
    public List<UserProfileDTO> getProfilesByRole(String role) {
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role must be provided");
        }

        String upperRole = role.toUpperCase();
        Map<Long, UserProfileDTO> profileMap = new LinkedHashMap<>();

        // 1) local profiles first
        for (UserProfile profile : userProfileRepository.findByRole(upperRole)) {
            AuthUserDTO authUser = authServiceClient.getUserById(profile.getAuthUserId());
            if (authUser != null) {
                profileMap.put(authUser.getId(),
                        UserProfileMapper.toDTO(profile, authUser));
            }
        }

        // 2) then any auth-service-only
        for (AuthUserDTO authUser : authServiceClient.getUsersByRole(upperRole)) {
            profileMap.computeIfAbsent(authUser.getId(),
                    id -> UserProfileMapper.toDTO(null, authUser));
        }

        return new ArrayList<>(profileMap.values());
    }

    @Override
    public String storePhoto(Long userId, MultipartFile file) throws IOException {
        if (userId == null) throw new IllegalArgumentException("UserId cannot be null");
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }

        Path photosDir = rootLocation.resolve("profile-photos");
        Files.createDirectories(photosDir);

        String original = Path.of(file.getOriginalFilename()).getFileName().toString();
        String ext = original.contains(".")
                   ? original.substring(original.lastIndexOf('.'))
                   : "";

        String filename = String.format("profile_%d%s", userId, ext);
        Path dest = photosDir.resolve(filename).normalize().toAbsolutePath();

        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        // delete old
        String oldUrl = profile.getProfileImageUrl();
        if (oldUrl != null && !oldUrl.isBlank()) {
            Path oldFile = rootLocation.resolve(oldUrl.replaceFirst("^/uploads/", ""));
            if (Files.exists(oldFile)) {
                Files.delete(oldFile);
            }
        }

        try (var in = file.getInputStream()) {
            Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
        }

        String relative = "/uploads/profile-photos/" + filename;
        profile.setProfileImageUrl(relative);
        userProfileRepository.save(profile);

        return relative;
    }
}
