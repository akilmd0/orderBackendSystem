package com.example.ecommerce.entity;

import com.example.ecommerce.enums.PaymentStatus;
import com.example.ecommerce.enums.PaymentType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Payments")
public class Payment {

    @Id
    private UUID paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)  // Foreign key to Users table
    private User user;

    @OneToOne
    @JoinColumn(name = "orderId", nullable = false, unique = true)  // Foreign key to Orders table
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;
}
