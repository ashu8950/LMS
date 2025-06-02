package com.auth_service.controllers;

import com.auth_service.dto.*;
import com.auth_service.security.CustomUserDetails;
import com.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
        authService.generateOtpAndSaveRegistration(request);
        return ResponseEntity.ok(new ApiResponse<>(true,
            "OTP sent to your email. Verify to complete registration.", null));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyOtp(@Valid @RequestBody OtpVerifyRequest request) {
        AuthResponse authResponse = authService.verifyOtpAndRegister(request.getOtp());
        return ResponseEntity.ok(new ApiResponse<>(true, "Registration successful", authResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody TokenRefreshRequest request) {
        AuthResponse authResponse = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody LogoutRequest request) {
        authService.revokeToken(request.getAccessToken());
        return ResponseEntity.ok(new ApiResponse<>(true, "Logged out successfully", null));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.initiatePasswordReset(request.getEmail());
        return ResponseEntity.ok(new ApiResponse<>(true,
            "OTP sent to your email for password reset", null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getOtp(), request.getNewPassword());
        return ResponseEntity.ok(new ApiResponse<>(true, "Password reset successful", null));
    }


}
