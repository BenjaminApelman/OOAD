package com.example.microservices.mediator;

import com.example.microservices.logging.MethodCallLogger;
import com.example.microservices.model.Request;
import com.example.microservices.model.Response;
import com.example.microservices.model.ServiceIdentifier;
import com.example.microservices.services.BaseService;

import java.util.HashMap;
import java.util.Map;

/**
 * A Mediator that centralizes communication between services.
 * Can enforce load balancing, routing, and monitoring traffic.
 */
public class ServiceMediator {
    private final Map<ServiceIdentifier, BaseService> services = new HashMap<>();
    private final MethodCallLogger logger;

    public ServiceMediator(MethodCallLogger logger) {
        this.logger = logger;
    }

    public void registerService(BaseService service) {
        services.put(service.getIdentifier(), service);
    }

    public Response dispatchRequest(ServiceIdentifier identifier, Request request) {
        BaseService service = services.get(identifier);
        if (service == null) {
            return new Response("Service not found", 404);
        }

        // In a more complex scenario, the mediator would:
        // - Check load conditions
        // - Possibly reroute requests or enforce backpressure
        // - Log the request dispatch
        // For now, just forward the request.
        return service.handleRequest(request);
    }
}

