// dto/VerifyPaymentRequest.java
package com.payment_service.dto;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyPaymentRequest {
    private String orderId;
    private String paymentId;
    private String signature;
}
