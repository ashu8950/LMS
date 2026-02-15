package com.auth_service.service;

import com.auth_service.dto.*;
import com.auth_service.entity.User;
import com.auth_service.enums.Role;
import com.auth_service.repository.UserRepository;
import com.auth_service.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final OtpService otpService;
    private final RabbitMQSender rabbitMQSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    private static final SecureRandom secureRandom = new SecureRandom();

    private String generateOtp() {
        return String.valueOf(secureRandom.nextInt(900_000) + 100_000);
    }

    // 1) Registration step 1: send OTP
    public void generateOtpAndSaveRegistration(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalStateException("User already exists");
        }

        // Restrict admin registration to only one user
        if (Role.ADMIN.equals(req.getRole())) {
            boolean adminExists = userRepository.existsByRole(Role.ADMIN);
            if (adminExists) {
                throw new IllegalStateException("Admin user already registered. Only one admin allowed.");
            }
        }


        // Proceed for all other roles (INSTRUCTOR, STUDENT, etc.)
        String otp = generateOtp();
        otpService.saveOtp(req.getEmail(), otp);
        otpService.saveRegistrationData(req.getEmail(), req);
        rabbitMQSender.sendOtpMessage(req.getEmail(), otp);
    }

    // 2) Registration step 2: verify OTP & register user
    public AuthResponse verifyOtpAndRegister(String otp) {
        if (otp == null || otp.isEmpty()) {
            throw new IllegalArgumentException("OTP must not be null or empty");
        }
        String email = otpService.getEmailByOtp(otp);
        log.info("Verifying OTP. Email found: {}", email);
        if (email == null) {
            throw new IllegalArgumentException("Invalid or expired OTP");
        }

        otpService.deleteOtp(otp);
        RegisterRequest savedReq = otpService.getRegistrationData(email);
        if (savedReq == null) {
            log.error("No pending registration found for email: {}", email);
            throw new IllegalStateException("No pending registration for " + email);
        }

        log.info("Registration data retrieved for email: {}. Username: '{}', Password length: {}, Role: {}",
                 email,
                 savedReq.getUserName(),
                 savedReq.getPassword() != null ? savedReq.getPassword().length() : 0,
                 savedReq.getRole());

        // Validate that username is not null or empty
        if (savedReq.getUserName() == null || savedReq.getUserName().trim().isEmpty()) {
            log.error("Username is null or empty in registration data for email: {}", email);
            throw new IllegalStateException("Username is missing from registration data");
        }

        otpService.deleteRegistrationData(email);

        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("User already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(savedReq.getPassword()));
        user.setRole(savedReq.getRole());
        user.setUsername(savedReq.getUserName());
        user.setEnabled(true);

        log.info("About to save user with username: '{}', email: '{}', role: {}",
                 user.getUsername(), user.getEmail(), user.getRole());

        try {
            userRepository.save(user);
            log.info("User successfully saved with ID: {}, username: '{}'", user.getId(), user.getUsername());
        } catch (Exception e) {
            log.error("Failed to save user. Username: '{}', Email: '{}'. Error: {}",
                     user.getUsername(), user.getEmail(), e.getMessage(), e);
            throw e;
        }

        AuthResponse response = authenticateAndBuildResponse(user, savedReq.getPassword());
        rabbitMQSender.sendWelcomeMessage(email, savedReq.getUserName());
        return response;
    }

    // 3) Login existing user
    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return authenticateAndBuildResponse(user, request.getPassword());
    }

    // 4) Refresh access token
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }
        String email = jwtTokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String newAccessToken = jwtTokenProvider.generateToken(userDetails,user.getId());

        // replace only the ACCESS token
        tokenService.upsertUserToken(
            user,
            newAccessToken,
            "ACCESS",
            jwtTokenProvider.getExpiration(newAccessToken)
        );

        return new AuthResponse(
            newAccessToken,
            refreshToken,
            user.getRole(),
            user.getEmail(),
            user.getId()
        );
    }

    // 5) Logout: revoke one access token
    public void revokeToken(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException("Invalid or expired access token");
        }
        tokenService.revokeToken(accessToken);
    }

    // 6) Forgot Password: send OTP for reset
    public void initiatePasswordReset(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("No user registered with this email");
        }
        String otp = generateOtp();
        otpService.saveOtp(email, otp);
        rabbitMQSender.sendOtpMessage(email, otp);
    }

    // 7) Reset Password: validate OTP and update password
    public void resetPassword(String otp, String newPassword) {
        if (otp == null || otp.isEmpty()) {
            throw new IllegalArgumentException("OTP must not be null or empty");
        }
        String email = otpService.getEmailByOtp(otp);
        if (email == null) {
            throw new IllegalArgumentException("Invalid or expired OTP");
        }
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        otpService.deleteOtp(otp);

        // invalidate all tokens after password change
        tokenService.revokeAllUserTokens(user);
    }

    // Shared: authenticate & build response (registration & login)
    private AuthResponse authenticateAndBuildResponse(User user, String rawPassword) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getEmail(), rawPassword)
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        String accessToken  = jwtTokenProvider.generateToken(userDetails,user.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        // replace both ACCESS & REFRESH tokens
        tokenService.upsertUserToken(
            user,
            accessToken,
            "ACCESS",
            jwtTokenProvider.getExpiration(accessToken)
        );
        tokenService.upsertUserToken(
            user,
            refreshToken,
            "REFRESH",
            jwtTokenProvider.getExpiration(refreshToken)
        );

        return new AuthResponse(
            accessToken,
            refreshToken,
            user.getRole(),
            user.getEmail(),
            user.getId()
        );
    }
}
