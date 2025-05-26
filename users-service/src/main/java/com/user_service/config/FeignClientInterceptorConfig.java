package com.user_service.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@Slf4j
public class FeignClientInterceptorConfig {

    @Bean
    public RequestInterceptor authFeignRequestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String authHeader = request.getHeader("Authorization");
                if (authHeader != null) {
                    requestTemplate.header("Authorization", authHeader);
                    log.info("Forwarding Authorization header to auth-service: {}", authHeader);
                } else {
                    log.warn("No Authorization header found in the incoming request!");
                }
            } else {
                log.warn("Could not get ServletRequestAttributes from RequestContextHolder");
            }
        };
    }
}
