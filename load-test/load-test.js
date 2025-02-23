import http from 'k6/http';
import { check } from 'k6';

export let options = {
    stages: [
        { duration: '10s', target: 100 },  // Ramp up to 100 requests in 10 seconds
        { duration: '20s', target: 1000 }, // Ramp up to 1000 requests in 20 seconds
        { duration: '10s', target: 1000 }, // Keep 1000 requests for 10 seconds
        { duration: '10s', target: 0 },    // Ramp down to 0 requests
    ],
};

export default function () {
    let createUserUrl = 'http://localhost:8080/users';
    let userPayload = JSON.stringify({
        name: "John Doe",
        email: "john.doe@example.com",
        mobNumber: "9876543210"
    });

    let userParams = {
        headers: { 'Content-Type': 'application/json' }
    };

    let userRes = http.post(createUserUrl, userPayload, userParams);
    check(userRes, { "user created successfully": (r) => r.status === 201 });

    // Extract userId from the response
    let userId = JSON.parse(userRes.body).userId;

    let createOrderUrl = 'http://localhost:8080/orders';
    let randomItemId = "c7dbb13f-eb23-4c52-a86c-fbbd5c30ee54";

    let orderPayload = JSON.stringify({
        userId: userId,
        itemIds: [randomItemId],
        totalAmount: 100.50
    });

    let orderRes = http.post(createOrderUrl, orderPayload, userParams);
    check(orderRes, { "order created successfully": (r) => r.status === 201 });
}
