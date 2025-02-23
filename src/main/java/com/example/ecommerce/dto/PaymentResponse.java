package com.example.ecommerce.dto;


import com.example.ecommerce.enums.PaymentStatus;
import com.example.ecommerce.enums.PaymentType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    String paymentId;
    PaymentType paymentType;
    PaymentStatus paymentStatus;
    String userId;
    String orderId;

}
