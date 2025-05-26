// entity/Payment.java
package com.payment_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;        // Razorpay order ID
    private Long paymentId;      // Razorpay payment ID
    private String signature;    // For verification

    private Integer amount;      // in paise
    private String currency;
    private String status;       // created, authorized, captured

    private LocalDateTime createdAt;
    @PrePersist
    public void prePersist() { createdAt = LocalDateTime.now(); }
}
