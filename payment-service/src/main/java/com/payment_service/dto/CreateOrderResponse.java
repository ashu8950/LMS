// dto/CreateOrderResponse.java
package com.payment_service.dto;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderResponse {
    private String orderId;
    private Integer amount;
    private String currency;
}
