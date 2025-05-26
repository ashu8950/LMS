// service/PaymentService.java
package com.payment_service.service;

import com.payment_service.dto.CreateOrderRequest;
import com.payment_service.dto.CreateOrderResponse;
import com.payment_service.dto.VerifyPaymentRequest;
import com.payment_service.dto.VerifyPaymentResponse;

public interface PaymentService {
    CreateOrderResponse createOrder(CreateOrderRequest req) throws Exception;
    VerifyPaymentResponse verifyPayment(VerifyPaymentRequest req) throws Exception;
}
