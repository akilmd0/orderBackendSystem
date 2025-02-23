package com.example.ecommerce.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    String paymentId;
    String paymentType;
    String userId;
    String orderId;

}
