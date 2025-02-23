package com.example.ecommerce.service;

import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.enums.OrderStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderMetricsService {

    private final OrderRepository orderRepository;

    public OrderMetricsService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public long getTotalOrders() {
        return orderRepository.count();
    }

    public long getPendingOrders() {
        return orderRepository.countByStatus(OrderStatus.PROCESSING);
    }

    public long getCompletedOrders() {
        return orderRepository.countByStatus(OrderStatus.COMPLETED);
    }
    public double getAverageProcessingTime() {
        return orderRepository.findAll().stream()
                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                .mapToLong(order -> Duration.between(order.getCreatedAt(), order.getUpdatedAt()).toMillis())
                .average()
                .orElse(0.0);
    }

    public Map<String, Long> getOrderStatusCounts() {
        return orderRepository.findAll().stream()
                .collect(Collectors.groupingBy(order -> order.getStatus().name(), Collectors.counting()));
    }
}
