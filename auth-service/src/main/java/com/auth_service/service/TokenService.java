package com.auth_service.service;

import com.auth_service.entity.Token;
import com.auth_service.entity.User;
import com.auth_service.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final TokenRepository tokenRepository;

    /**
     * Inserts or replaces the single active token of given type for a user.
     */
    @Transactional
    public void upsertUserToken(User user, String jwtToken, String tokenType, LocalDateTime expiresAt) {
        // remove existing
        tokenRepository.deleteAllByUserAndTokenType(user, tokenType);

        // insert new
        Token token = new Token();
        token.setToken(jwtToken);
        token.setUser(user);
        token.setExpired(false);
        token.setRevoked(false);
        token.setTokenType(tokenType);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(expiresAt);
        tokenRepository.save(token);

        log.debug("Upserted {} token for user {}", tokenType, user.getEmail());
    }

    /**
     * Revoke all active tokens of this user (any type).
     */
    @Transactional
    public void revokeAllUserTokens(User user) {
        tokenRepository.findAllByUserAndExpiredIsFalseAndRevokedIsFalse(user)
            .forEach(t -> {
                t.setExpired(true);
                t.setRevoked(true);
            });
        log.debug("Revoked all tokens for user {}", user.getEmail());
    }

    /**
     * Revoke a single token by its string value.
     */
    @Transactional
    public void revokeToken(String jwtToken) {
        Token token = tokenRepository.findByToken(jwtToken)
            .orElseThrow(() -> new IllegalArgumentException("Token not found: " + jwtToken));
        token.setExpired(true);
        token.setRevoked(true);
        log.debug("Revoked token for user {}", token.getUser().getEmail());
    }

    /**
     * Check if a given token is still valid.
     */
    public boolean isTokenValid(String jwtToken) {
        return tokenRepository.findByToken(jwtToken)
            .filter(t -> !t.isExpired() && !t.isRevoked())
            .isPresent();
    }

    
    @Scheduled(cron = "0 0 * * * *") // every hour
    @Transactional
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        tokenRepository.deleteAllByExpiresAtBefore(now);
        log.debug("Cleaned up tokens expired before {}", now);
    }
}
