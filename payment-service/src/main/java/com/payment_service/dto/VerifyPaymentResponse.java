// dto/VerifyPaymentResponse.java
package com.payment_service.dto;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyPaymentResponse {
    private boolean valid;
    private String status;
}
