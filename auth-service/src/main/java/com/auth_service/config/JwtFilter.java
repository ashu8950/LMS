package com.auth_service.config;

import com.auth_service.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public JwtFilter(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Skip only truly public auth endpoints
        return path.equals("/auth/login")
            || path.equals("/auth/register")
            || path.equals("/auth/verify-otp")
            || path.equals("/auth/refresh")
            || path.equals("/auth/forgot-password")
            || path.equals("/auth/reset-password")
            || path.equals("/auth/logout");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
            Claims claims = Jwts.parserBuilder()
                                .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                                .build()
                                .parseClaimsJws(token)
                                .getBody();

            String email = claims.getSubject();
            Integer userIdClaim = claims.get("userId", Integer.class);
            @SuppressWarnings("unchecked")
            List<String> roles = claims.get("roles", List.class);

            // Load user details (you may choose to ignore DB lookup and create custom details with claims)
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            var authorities = roles.stream()
                                   .map(SimpleGrantedAuthority::new)
                                   .collect(Collectors.toList());

            // Build authentication token, include userId in details if needed
            var authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                authorities);
            authToken.setDetails(userIdClaim); // store userId as detail

            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
