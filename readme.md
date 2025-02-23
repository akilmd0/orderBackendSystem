Overview

    I have implememted all the functionality that was asked in the assignment with some additional features mention below.

Features

1. Create orders, get order details and track their status.
2. Payment integration for each order.
3. Redis caching for efficient state management.
4. Order status tracking with various stages (e.g., Pending, Paid).
5. Scalable and maintainable design.
6. Create users and get user details.
7. Metrics api to get all the orders and to get all orders of a particular user.
8. Get latency of all orders as well as get order status counts.

Architecture

    OrderService: Manages order creation and status updates.
    PaymentService: Handles payments for orders and updates the order status upon success.
    RedisService: Caches the transaction status to improve performance.
    Database: PostgreSQL database with tables for orders, payments, orderItems and users.
    Queue: In-memory queue to process the orders and payments.
    LoadTestService: Tests load on the server for thousand of requests at a time.

Project Structure

src/main/java/com/example/ecommerce/
│── controller/
│   ├── OrderController.java     # Handles incoming API requests
│   ├── UserController.java     # Handles incoming API requests
│   ├── MetricsController.java     # Handles incoming API requests
|
│── queue/
│   ├── OrderProcessingService.java  # Processes orders and payments
│
│── repository/
│   ├── OrderRepository.java    # Interfaces with the PostgreSQL database
│   ├── PaymentRepository.java    # Interfaces with the PostgreSQL database
│   ├── UserRepository.java    # Interfaces with the PostgreSQL database
│   ├── OrderItemsRository.java    # Interfaces with the PostgreSQL database
|
│── entity/
│   ├── Orders.java              # Represents the Order entity
│   ├── Users.java              # Represents the Order entity
│   ├── OrderItems.java              # Represents the Order entity
│   ├── Payment.java              # Represents the Order entity
│
│── enums/
│   ├── OrderStatus.java        # Defines the order statuses (PROCESSING, COMPLETED, etc.)
│   ├── PaymentStatut.java        # Defines the payment statuses (Pending, Paid, etc.)
│   ├── PaymentType.java        # Defines the  payment type (CARD, Paid, etc.)
│
│── redis/
│   ├── RedisService.java       # Handles Redis-related operations
│   ├── RedisConfiguration.java       # Handles Redis-related operations
│
│── EcommerceApplication.java   # Main entry point of the application

Installation:

Prerequisites:

    Java 23 or above
    Spring Boot 3.4.3
    PostgreSQL (configured in the database connection properties)
    Redis (configured for caching)
    Maven(optional)
    K6 (For load Testing)
    Pgcrypto for uuid generation


Build the project:

./mvnw clean install or mvn if you haven maven install

Run the application:

./mvnw spring-boot:run

API Endpoints --- I have added postaman schema in ecommerce folder

Database Schema --- I have added schema.sql in resource folder

Redis Caching -- used for caching and also for the payments status update

The application utilizes Redis to cache payment transaction status for faster response times. Redis operations are handled by the RedisService class.

Future Enhancements
Add user authentication and authorization.
Implement retry mechanisms for payment failures.
Add detailed error handling and logging.
Create table for items.
Correct table names due to some conflict and use small letters in all the table and column names.
Add test cases.