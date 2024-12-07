package com.example.microservices.services;

import com.example.microservices.logging.MethodCallLogger;
import com.example.microservices.model.Request;
import com.example.microservices.model.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {
    @Test
    void testPlaceOrder() {
        MethodCallLogger logger = new MethodCallLogger();
        OrderService service = new OrderService(logger);

        Request req = new Request("PlaceOrder", "Item=ABC;Quantity=1");
        Response res = service.handleRequest(req);

        assertEquals(200, res.getStatusCode());
        assertEquals("Order placed successfully", res.getMessage());
        assertEquals(1, logger.getRecords().size());
    }

    @Test
    void testUnknownOperation() {
        MethodCallLogger logger = new MethodCallLogger();
        OrderService service = new OrderService(logger);

        Request req = new Request("UnknownOp", "param");
        Response res = service.handleRequest(req);

        assertEquals(400, res.getStatusCode());
    }
}
