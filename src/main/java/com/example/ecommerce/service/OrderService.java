package com.example.ecommerce.service;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.dto.OrderRequest;
import com.example.ecommerce.dto.OrderResponse;
import com.example.ecommerce.exception.OrderNotFoundException;
import com.example.ecommerce.queue.OrderProcessingService;
import com.example.ecommerce.redis.RedisService;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderProcessingService orderProcessingService;
    private final RedisService redisService;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        OrderProcessingService orderProcessingService, RedisService redisService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderProcessingService = orderProcessingService;
        this.redisService = redisService;
    }

    public OrderResponse createOrder(OrderRequest orderRequest) {
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + orderRequest.getUserId()));

        Order order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setUser(user);
        order.setStatus(OrderStatus.PROCESSING);
        order.setTotalAmount(orderRequest.getTotalAmount());
        order.setCreatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));

        System.out.println("Calling queueOrder with order: " + order.getOrderId());

        orderProcessingService.queueOrder(order);
        String paymentStatus = redisService.getKey(order.getOrderId().toString(), 2L);
        if (paymentStatus != null) {
            order.setStatus(OrderStatus.valueOf(paymentStatus));
        }
        return new OrderResponse(order.getOrderId(), order.getUser().getUserId(), order.getStatus(), order.getCreatedAt());
    }

    public Optional<Order> getOrder(UUID orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getAllOrdersByUserId(UUID userId) {
        return orderRepository.findAllByUser_UserId(userId);
    }

    public long getTotalOrdersProcessed() {
        return orderRepository.count();
    }

    public OrderStatus getOrderStatus(UUID orderId) {
        return orderRepository.findById(orderId)
                .map(Order::getStatus)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
    }

    public long countOrdersByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    public Optional<Order> getOrderById(UUID orderId) {
        return orderRepository.findById(orderId);
    }
}
