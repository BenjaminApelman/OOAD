package com.example.microservices;

import com.example.microservices.logging.MethodCallLogger;
import com.example.microservices.mediator.ServiceMediator;
import com.example.microservices.model.Request;
import com.example.microservices.model.ServiceIdentifier;
import com.example.microservices.services.OrderService;
import com.example.microservices.services.PaymentService;

public class Main {
    public static void main(String[] args) {
        // Initialize a global logger
        MethodCallLogger logger = new MethodCallLogger();

        // Create services
        OrderService orderService = new OrderService(logger);
        PaymentService paymentService = new PaymentService(logger);

        // Create mediator
        ServiceMediator mediator = new ServiceMediator(logger);
        mediator.registerService(orderService);
        mediator.registerService(paymentService);

        // Simulate a request that flows through the mediator to the OrderService
        Request request = new Request("PlaceOrder", "Item=ABC;Quantity=1");
        mediator.dispatchRequest(ServiceIdentifier.ORDER, request);

        // Simulate a request that flows through the mediator to the PaymentService
        Request paymentRequest = new Request("ProcessPayment", "OrderID=123;Amount=100");
        mediator.dispatchRequest(ServiceIdentifier.PAYMENT, paymentRequest);
    }
}

