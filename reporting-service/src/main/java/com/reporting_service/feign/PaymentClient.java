package com.reporting_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.reporting_service.dto.PaymentDto;

import java.util.List;

@FeignClient(name = "payment-service")
public interface PaymentClient {
    @GetMapping("/payment/status")
    List<PaymentDto> getPaymentsForUser(@RequestParam("userId") Long userId);
}