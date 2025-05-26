package com.auth_service.repository;

import com.auth_service.entity.Token;
import com.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    List<Token> findAllByUserAndExpiredIsFalseAndRevokedIsFalse(User user);

    Optional<Token> findByToken(String token);

    @Transactional
    void deleteAllByUserAndTokenType(User user, String tokenType);

    @Transactional
    void deleteAllByExpiresAtBefore(LocalDateTime cutoff);

}
