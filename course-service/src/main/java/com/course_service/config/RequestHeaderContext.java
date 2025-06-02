package com.course_service.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RequestHeaderContext {

    public String getUserId(HttpServletRequest request) {
        return request.getHeader("X-User-Id");
    }

    public String getUserEmail(HttpServletRequest request) {
        return request.getHeader("X-User-Email");
    }

    public List<String> getUserRoles(HttpServletRequest request) {
        String roles = request.getHeader("X-User-Roles");
        return roles != null ? Arrays.asList(roles.split(",")) : List.of();
    }

    public boolean hasRole(HttpServletRequest request, String role) {
        return getUserRoles(request).contains(role);
    }
}
