package com.example.ecommerce.queue;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.redis.RedisService;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.service.PaymentService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class OrderProcessingService {
    private final OrderRepository orderRepository;
    private final BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();
    private final PaymentService paymentService;
    private final RedisService redis;

    public OrderProcessingService(OrderRepository orderRepository, PaymentService paymentService , RedisService redis) {
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
        this.redis = redis;
    }

    public void queueOrder(Order order) {
        try {
            System.out.println("DEBUG: queueOrder() called with Order ID: " + order.getOrderId());

            // Ensure the order does not already exist
            if (orderRepository.findById(order.getOrderId()).isEmpty()) {
                System.out.println("INFO: Order not found, saving new order: " + order.getOrderId());
                order.setOrderId(null); // Ensure Hibernate treats it as new
                order = orderRepository.save(order);
            }

            System.out.println("DEBUG: Successfully saved order: " + order.getOrderId());
            orderQueue.offer(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @PostConstruct
    public void startOrderProcessingThread() {
        Thread workerThread = new Thread(this::processOrders);
        workerThread.setDaemon(true); // Ensures it stops when the application stops
        workerThread.start();
    }

    private void processOrders() {
        while (true) {
            try {
                Order order = orderQueue.take(); // Blocks if queue is empty
                processOrder(order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private synchronized void processOrder(Order order) {
            boolean result = paymentService.makePayment(order);
            System.out.println("processing the order"+order.getOrderId()+result);
            if(result) {
                System.out.println("got the payment successfull result "+result);
                order.setStatus(OrderStatus.COMPLETED);
                order.setUpdatedAt(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
                redis.setKey(order.getOrderId().toString(), order.getStatus().name(), 2L);
                orderRepository.save(order);
        }
    }
}
