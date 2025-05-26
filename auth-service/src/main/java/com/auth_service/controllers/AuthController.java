package com.auth_service.controllers;

import com.auth_service.dto.*;
import com.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Register: generate OTP and save pending registration
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
        authService.generateOtpAndSaveRegistration(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "OTP sent to your email. Verify to complete registration.", null));
    }

    // Verify OTP and complete registration
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyOtp(@Valid @RequestBody OtpVerifyRequest request) {
        AuthResponse authResponse = authService.verifyOtpAndRegister(request.getOtp());
        return ResponseEntity.ok(new ApiResponse<>(true, "Registration successful", authResponse));
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(authResponse);
    }

    // Refresh JWT token
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody TokenRefreshRequest request) {
        AuthResponse authResponse = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(authResponse);
    }

    // Logout and revoke tokens
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody LogoutRequest request) {
        authService.revokeToken(request.getAccessToken());
        return ResponseEntity.ok(new ApiResponse<>(true, "Logged out successfully", null));
    }

    // Initiate forgot password process - send OTP to email
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.initiatePasswordReset(request.getEmail());
        return ResponseEntity.ok(new ApiResponse<>(true, "OTP sent to your email for password reset", null));
    }

    // Reset password using OTP
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getOtp(), request.getNewPassword());
        return ResponseEntity.ok(new ApiResponse<>(true, "Password reset successful", null));
    }

    // Simple test endpoint
    @GetMapping
    public String hello() {
        return "Hello ashu";
    }
}
