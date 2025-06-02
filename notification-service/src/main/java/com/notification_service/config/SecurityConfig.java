package com.notification_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for non-browser clients or stateless services
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll() // Permit all endpoints
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults()) // Can leave disabled if you prefer
            .formLogin(form -> form.disable()); // Disable form login

        return http.build();
    }
}
