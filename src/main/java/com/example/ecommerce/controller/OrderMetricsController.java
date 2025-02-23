package com.example.ecommerce.controller;

import com.example.ecommerce.service.OrderMetricsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/orders/metrics")
public class OrderMetricsController {

    private final OrderMetricsService orderMetricsService;

    public OrderMetricsController(OrderMetricsService orderMetricsService) {
        this.orderMetricsService = orderMetricsService;
    }

    @GetMapping("/totalOrders")
    public ResponseEntity<Long> getTotalOrders() {
        long totalOrders = orderMetricsService.getTotalOrders();
        return ResponseEntity.ok(totalOrders);
    }

    @GetMapping("/avgProcessingTime")
    public ResponseEntity<Double> getAverageProcessingTime() {
        double avgTime = orderMetricsService.getAverageProcessingTime();
        return ResponseEntity.ok(avgTime);
    }

    @GetMapping("/statusCounts")
    public ResponseEntity<Map<String, Long>> getOrderStatusCounts() {
        Map<String, Long> statusCounts = orderMetricsService.getOrderStatusCounts();
        return ResponseEntity.ok(statusCounts);
    }
}
