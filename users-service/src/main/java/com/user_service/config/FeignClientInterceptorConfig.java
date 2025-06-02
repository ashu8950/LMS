//package com.user_service.config;
//
//import feign.RequestInterceptor;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//@Configuration
//@Slf4j
//public class FeignClientInterceptorConfig {
//
//    @Bean
//    public RequestInterceptor authFeignRequestInterceptor() {
//        return requestTemplate -> {
//            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            if (attributes == null) {
//                log.warn("No ServletRequestAttributes found — skipping Authorization header forwarding");
//                return;
//            }
//
//            HttpServletRequest request = attributes.getRequest();
//            if (request == null) {
//                log.warn("No HttpServletRequest available — cannot forward Authorization header");
//                return;
//            }
//
//            String authHeader = request.getHeader("Authorization");
//            if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                requestTemplate.header("Authorization", authHeader);
//                log.debug("Forwarding Authorization header to downstream: {}", authHeader);
//            } else {
//                log.warn("Authorization header missing or invalid — not forwarded");
//            }
//        };
//    }
//}
