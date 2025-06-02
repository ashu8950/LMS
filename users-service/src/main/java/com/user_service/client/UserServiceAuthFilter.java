//package com.user_service.client;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import com.user_service.dto.UserPrincipal;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class UserServiceAuthFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String userIdHeader = request.getHeader("X-User-Id");
//        String usernameHeader = request.getHeader("X-Username");
//        String rolesHeader = request.getHeader("X-Roles");
//
//        if (userIdHeader != null && usernameHeader != null && rolesHeader != null) {
//            try {
//                Long userId = Long.valueOf(userIdHeader);
//                List<SimpleGrantedAuthority> authorities = Arrays.stream(rolesHeader.split(","))
//                        .map(String::trim)
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
//
//                UserPrincipal principal = new UserPrincipal(userId, usernameHeader);
//
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(principal, null, authorities);
//
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            } catch (NumberFormatException e) {
//                // Invalid userId header - ignore or log
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return;
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
