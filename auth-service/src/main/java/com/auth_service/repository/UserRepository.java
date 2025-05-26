package com.auth_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth_service.entity.User;
import com.auth_service.enums.Role;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByRole(Role role);

    Optional<User> findByRole(Role role);  // for single user with a given role

    List<User> findAllByRole(Role role);   // for all users with a given role
}
