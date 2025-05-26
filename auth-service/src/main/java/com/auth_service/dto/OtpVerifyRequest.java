package com.auth_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OtpVerifyRequest {

    @NotNull(message = "OTP must not be null")
    @Size(min = 4, max = 6, message = "OTP must be between 4 and 6 characters")
    private String otp;
}
