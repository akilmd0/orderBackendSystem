package com.example.ecommerce.service;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.Payment;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.enums.PaymentStatus;
import com.example.ecommerce.enums.PaymentType;
import com.example.ecommerce.repository.PaymentRepository;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final StringRedisTemplate redisTemplate;
    private final PaymentRepository paymentRepository;
    @PersistenceContext  // Injects the EntityManager
    private EntityManager entityManager;

    @Transactional
    public boolean makePayment(Order order) {
        try {
            String paymentResponse = redisTemplate.opsForValue().get("paymentResponse");
            boolean isPaymentSuccessful = "SUCCESS".equalsIgnoreCase(paymentResponse != null ? paymentResponse : "SUCCESS");

            Payment payment = paymentRepository.findByOrder_OrderId(order.getOrderId()).orElse(new Payment());
            if (payment.getPaymentId() == null) payment.setPaymentId(UUID.randomUUID());

            System.out.println("printing user class "+order.getUser());
            payment.setOrder(order);
            payment.setPaymentStatus(isPaymentSuccessful ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
            payment.setPaymentType(PaymentType.valueOf(redisTemplate.opsForValue().get("paymentType") != null ? redisTemplate.opsForValue().get("paymentType") : "CARD"));
            payment.setUser(entityManager.getReference(User.class, order.getUser().getUserId()));

            paymentRepository.save(payment);
            return isPaymentSuccessful;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}