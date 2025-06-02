package com.gateway_service.util;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.function.Function;


import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    private static final String SECRET_KEY = "my-secret-key-that-is-long-enough-to-meet-the-length-requirement";
    private static final long EXPIRATION_TIME_MS = 86400000L; // 24 hours

    private final Key signingKey;

    public JwtTokenProvider() {
        this.signingKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

   

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException ex) {
            System.out.println("JWT expired: " + ex.getMessage());
        } catch (JwtException ex) {
            System.out.println("Invalid JWT: " + ex.getMessage());
        }
        return false;
    }

    // Extract username
    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract roles
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }

    // Generic claim extractor
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseClaims(token);
        return claimsResolver.apply(claims);
    }

    // Parse JWT claims
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract expiration time as LocalDateTime
    public LocalDateTime getExpiration(String token) {
        Date expirationDate = extractClaim(token, Claims::getExpiration);
        if (expirationDate == null) {
            return null;
        }
        Instant instant = expirationDate.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
