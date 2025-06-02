package com.auth_service.service;

import com.auth_service.entity.User;
import com.auth_service.repository.UserRepository;
import com.auth_service.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new CustomUserDetails(
            user.getId(),
            user.getEmail(),
            user.getPassword(),
            user.isEnabled(),
            Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
