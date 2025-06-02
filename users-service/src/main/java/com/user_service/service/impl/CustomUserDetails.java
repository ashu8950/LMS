package com.user_service.service.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.user_service.entity.UserProfile;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final UserProfile user;

    public CustomUserDetails(UserProfile user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
       
        return null;
    }

    @Override
    public String getUsername() {
        return user.getEmail(); 
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; 
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // change based on your user status
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // change based on your user status
    }

    @Override
    public boolean isEnabled() {
        return true; // change based on your user status
    }

    // Optionally expose the UserProfile if needed elsewhere
    public UserProfile getUserProfile() {
        return user;
    }
}
