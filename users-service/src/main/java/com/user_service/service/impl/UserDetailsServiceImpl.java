package com.user_service.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.user_service.repository.UserProfileRepository;
import com.user_service.entity.UserProfile;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserProfileRepository userRepository;

    public UserDetailsServiceImpl(UserProfileRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserProfile user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }
}
