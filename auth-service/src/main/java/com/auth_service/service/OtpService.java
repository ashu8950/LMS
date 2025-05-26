package com.auth_service.service;

import java.time.Duration;

import com.auth_service.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final Duration TTL = Duration.ofMinutes(5);
    private static final String OTP_CODE_KEY_PREFIX = "otp_code:";      // otp_code:<otp>
    private static final String REGISTRATION_KEY_PREFIX = "reg:";       // reg:<email>

    // Save OTP as key, email as value
    public void saveOtp(String email, String otp) {
        if (email != null && otp != null) {
            redisTemplate.opsForValue().set(OTP_CODE_KEY_PREFIX + otp, email, TTL);
        }
    }

    // Get email by OTP key
    public String getEmailByOtp(String otp) {
        if (otp == null) return null;
        Object email = redisTemplate.opsForValue().get(OTP_CODE_KEY_PREFIX + otp);
        log.info("Email for OTP {}: {}", otp, email);
        return email != null ? email.toString() : null;
    }

    // Delete OTP key only by otp
    public void deleteOtp(String otp) {
        if (otp != null) {
            redisTemplate.delete(OTP_CODE_KEY_PREFIX + otp);
        }
    }

    // Save registration data keyed by email
    public void saveRegistrationData(String email, RegisterRequest req) {
        if (email != null && req != null) {
            redisTemplate.opsForValue().set(REGISTRATION_KEY_PREFIX + email, req, TTL);
        }
    }

    // Get registration data by email
    public RegisterRequest getRegistrationData(String email) {
        if (email == null) return null;
        Object data = redisTemplate.opsForValue().get(REGISTRATION_KEY_PREFIX + email);
        return data != null ? (RegisterRequest) data : null;
    }

    // Delete registration data by email
    public void deleteRegistrationData(String email) {
        if (email != null) {
            redisTemplate.delete(REGISTRATION_KEY_PREFIX + email);
        }
    }
}
