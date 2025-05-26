package com.payment_service.service.impl;

import com.payment_service.dto.CreateOrderRequest;
import com.payment_service.dto.CreateOrderResponse;
import com.payment_service.dto.VerifyPaymentRequest;
import com.payment_service.dto.VerifyPaymentResponse;
import com.payment_service.entity.Payment;
import com.payment_service.repository.PaymentRepository;
import com.payment_service.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final RazorpayClient razorpay;
    private final PaymentRepository repo;

    // Inject the Razorpay key secret from your application.yml
    @Value("${razorpay.key-secret}")
    private String keySecret;

    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest req) throws Exception {
        Map<String, Object> options = new HashMap<>();
        options.put("amount", req.getAmount());
        options.put("currency", req.getCurrency());
        options.put("receipt", req.getReceipt());

        Order order = razorpay.Orders.create(options);

        // Save a basic Payment record
        repo.save(Payment.builder()
            .orderId(order.get("id").toString())
            .amount((Integer)order.get("amount"))
            .currency(order.get("currency").toString())
            .status(order.get("status").toString())
            .build()
        );

        return CreateOrderResponse.builder()
            .orderId(order.get("id").toString())
            .amount((Integer)order.get("amount"))
            .currency(order.get("currency").toString())
            .build();
    }

    @Override
    public VerifyPaymentResponse verifyPayment(VerifyPaymentRequest req) throws Exception {
        // Use the injected keySecret here
        boolean isValid = Utils.verifyPaymentSignature(
            Map.of(
                "razorpay_order_id", req.getOrderId(),
                "razorpay_payment_id", req.getPaymentId()
            ),
            req.getSignature(),
            keySecret
        );

        Payment payment = repo.findByOrderId(req.getOrderId())
            .orElseThrow(() -> new RuntimeException("Order not found: " + req.getOrderId()));
        payment.setPaymentId(req.getPaymentId());
        payment.setSignature(req.getSignature());
        payment.setStatus(isValid ? "captured" : "failed");
        repo.save(payment);

        return VerifyPaymentResponse.builder()
            .valid(isValid)
            .status(payment.getStatus())
            .build();
    }
}
