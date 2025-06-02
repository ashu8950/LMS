package com.gateway_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import com.gateway_service.util.JwtTokenProvider;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Secure routes are validated centrally in the gateway
            if (validator.isSecured.test(exchange.getRequest())) {
                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    return Mono.error(new RuntimeException("Missing or invalid Authorization header"));
                }

                String token = authHeader.substring(7);

                // Validate the JWT here only once
                if (!jwtTokenProvider.validateToken(token)) {
                    return Mono.error(new RuntimeException("Invalid or expired token"));
                }

                // Extract required user details
                String email = jwtTokenProvider.getUsernameFromToken(token);
                String userId = String.valueOf(jwtTokenProvider.extractClaim(token, claims -> claims.get("userId", Integer.class)));
                var roles = jwtTokenProvider.getRolesFromToken(token);

                // Forward only extracted headers, removing Authorization for downstream simplicity
                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", userId)
                    .header("X-User-Email", email)
                    .header("X-User-Roles", String.join(",", roles))
                    .build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            }

            // Non-secured routes pass through unchanged
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // No custom properties needed
    }
}
