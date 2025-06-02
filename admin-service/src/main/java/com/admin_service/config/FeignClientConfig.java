package com.admin_service.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;

@Configuration
public class FeignClientConfig {
    @Bean
    public RequestInterceptor feignAuthInterceptor() {
        return template -> {
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            if (ra instanceof ServletRequestAttributes) {
                HttpServletRequest req = ((ServletRequestAttributes) ra).getRequest();
                String userId = req.getHeader("X‐User‐Id");
                if (userId != null) {
                    template.header("X‐User‐Id", userId);
                }
                String roles = req.getHeader("X‐User‐Roles");
                if (roles != null) {
                    template.header("X‐User‐Roles", roles);
                }
                String authz = req.getHeader("Authorization");
                if (authz != null) {
                    template.header("Authorization", authz);
                }
            }
        };
    }
}