package com.batch_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())  // Permit all requests
            .httpBasic(httpBasic -> httpBasic.disable())  // Disable HTTP Basic auth
            .formLogin(formLogin -> formLogin.disable()); // Disable form login

        return http.build();
    }
}
