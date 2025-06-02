package com.user_service.mapper;

import com.user_service.dto.AddressDTO;
import com.user_service.dto.AuthUserDTO;
import com.user_service.dto.UserProfileDTO;
import com.user_service.entity.Address;
import com.user_service.entity.UserProfile;

public class UserProfileMapper {

    // Address <-> AddressDTO conversions
    public static Address toAddressEntity(AddressDTO dto) {
        if (dto == null) return null;
        return Address.builder()
                .street(dto.getStreet())
                .city(dto.getCity())
                .state(dto.getState())
                .postalCode(dto.getPostalCode())
                .country(dto.getCountry())
                .build();
    }

    public static AddressDTO toAddressDTO(Address entity) {
        if (entity == null) return null;
        return AddressDTO.builder()
                .street(entity.getStreet())
                .city(entity.getCity())
                .state(entity.getState())
                .postalCode(entity.getPostalCode())
                .country(entity.getCountry())
                .build();
    }

    public static UserProfile toEntity(UserProfileDTO dto, AuthUserDTO authUser) {
        if (authUser == null) {
            throw new IllegalArgumentException("AuthUserDTO must not be null");
        }

        UserProfile profile = new UserProfile();

        // Use authUser as source of truth for these fields
        profile.setAuthUserId(authUser.getId());
        profile.setEmail(authUser.getEmail());
        profile.setRole(authUser.getRole());
        profile.setUsername(authUser.getUsername());

        if (dto != null) {
            // Profile-specific fields from DTO
            profile.setFirstName(dto.getFirstName());
            profile.setLastName(dto.getLastName());
            profile.setBio(dto.getBio());
            profile.setProfileImageUrl(dto.getProfileImageUrl());
            profile.setDob(dto.getDob());
            profile.setPhoneNumber(dto.getPhoneNumber());
            if (dto.getEnabled() != null) {
                profile.setEnabled(dto.getEnabled());
            }
            // Set Address entity if present
            profile.setAddress(toAddressEntity(dto.getAddress()));
            // Do NOT overwrite email or role from DTO; those come from authUser
        }

        return profile;
    }

    public static void updateEntity(UserProfile entity, UserProfileDTO dto, AuthUserDTO authUser) {
        if (entity == null) {
            throw new IllegalArgumentException("UserProfile entity must not be null");
        }
        if (dto != null) {
            if (dto.getFirstName() != null) {
                entity.setFirstName(dto.getFirstName());
            }
            if (dto.getLastName() != null) {
                entity.setLastName(dto.getLastName());
            }
            if (dto.getBio() != null) {
                entity.setBio(dto.getBio());
            }
            if (dto.getProfileImageUrl() != null) {
                entity.setProfileImageUrl(dto.getProfileImageUrl());
            }
            if (dto.getDob() != null) {
                entity.setDob(dto.getDob());
            }
            if (dto.getPhoneNumber() != null) {
                entity.setPhoneNumber(dto.getPhoneNumber());
            }
            if (dto.getEnabled() != null) {
                entity.setEnabled(dto.getEnabled());
            }
            // Update Address
            if (dto.getAddress() != null) {
                Address newAddress = toAddressEntity(dto.getAddress());
                if (entity.getAddress() == null) {
                    entity.setAddress(newAddress);
                } else {
                    // Update existing address fields
                    Address currentAddress = entity.getAddress();
                    currentAddress.setStreet(newAddress.getStreet());
                    currentAddress.setCity(newAddress.getCity());
                    currentAddress.setState(newAddress.getState());
                    currentAddress.setPostalCode(newAddress.getPostalCode());
                    currentAddress.setCountry(newAddress.getCountry());
                }
            }
            // Do NOT update email or role from DTO here (authUser is source of truth)
        }
        if (authUser != null) {
            // Always update these from authUser
            entity.setEmail(authUser.getEmail());
            entity.setRole(authUser.getRole());
            entity.setUsername(authUser.getUsername());
        }
    }

    public static UserProfileDTO toDTO(UserProfile entity, AuthUserDTO authUser) {
        if (entity == null && authUser == null) return null;

        UserProfileDTO dto = new UserProfileDTO();

        if (entity != null) {
            dto.setUserId(entity.getAuthUserId());
            dto.setFirstName(entity.getFirstName());
            dto.setLastName(entity.getLastName());
            dto.setBio(entity.getBio());
            dto.setProfileImageUrl(entity.getProfileImageUrl());
            dto.setDob(entity.getDob());
            dto.setPhoneNumber(entity.getPhoneNumber());
            dto.setEnabled(entity.getEnabled());
            // Address mapping
            dto.setAddress(toAddressDTO(entity.getAddress()));
            // Use email and role from entity only if authUser is not available
            dto.setEmail(entity.getEmail());
            dto.setRole(entity.getRole());
        }

        if (authUser != null) {
            // Override with latest authUser info if available
            dto.setEmail(authUser.getEmail());
            dto.setRole(authUser.getRole());
            dto.setUsername(authUser.getUsername());
        }

        return dto;
    }
}
