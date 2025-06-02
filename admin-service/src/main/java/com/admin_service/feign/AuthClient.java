package com.admin_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.admin_service.dto.AuthDto;
import com.admin_service.dto.OtpDto;


@FeignClient(name = "auth-service")
public interface AuthClient {
	 @PostMapping("/auth/register")
	  ResponseEntity<?> createUser(@RequestBody AuthDto authDto);
	 @PostMapping("/auth/verify-otp")
	ResponseEntity<?> verifyOtp(OtpDto otp);
}
