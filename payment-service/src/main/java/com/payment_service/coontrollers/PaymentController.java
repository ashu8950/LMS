package com.payment_service.coontrollers;



import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.payment_service.dto.CreateOrderRequest;
import com.payment_service.dto.CreateOrderResponse;
import com.payment_service.dto.VerifyPaymentRequest;
import com.payment_service.dto.VerifyPaymentResponse;
import com.payment_service.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService svc;

    @PostMapping("/order")
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest req) throws Exception {
        return ResponseEntity.ok(svc.createOrder(req));
    }

    @PostMapping("/verify")
    public ResponseEntity<VerifyPaymentResponse> verify(@RequestBody VerifyPaymentRequest req) throws Exception {
        return ResponseEntity.ok(svc.verifyPayment(req));
    }
}
