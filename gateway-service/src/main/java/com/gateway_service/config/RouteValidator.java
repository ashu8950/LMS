package com.gateway_service.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    // Public endpoints
    private static final List<String> OPEN_API_ENDPOINTS = List.of(
        "/auth/register",
        "/auth/login",
        "/auth/verify-otp",
        "/auth/refresh",
        "/auth/forgot-password",
        "/auth/reset-password",
        "/users/**"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // Returns true if the path is NOT in the list => requires authentication
    public final Predicate<ServerHttpRequest> isSecured = request -> 
        OPEN_API_ENDPOINTS.stream()
            .noneMatch(pattern -> pathMatcher.match(pattern, request.getURI().getPath()));
}
