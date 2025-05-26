package com.payment_service.dto;


import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private Integer amount;      // rupees *100
    private String currency;     // e.g. "INR"
    private String receipt;      // merchant reference
}
