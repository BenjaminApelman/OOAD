package com.example.microservices.logging;

import com.example.microservices.model.Request;
import com.example.microservices.model.Response;

import java.time.Instant;

public class ServiceCallRecord {
    private final String serviceName;
    private final Request request;
    private final Response response;
    private final int attemptNumber;
    private final Exception exception;
    private final Instant timestamp;

    public ServiceCallRecord(String serviceName, Request request, Response response, int attemptNumber) {
        this(serviceName, request, response, attemptNumber, null);
    }

    public ServiceCallRecord(String serviceName, Request request, Response response, int attemptNumber, Exception exception) {
        this.serviceName = serviceName;
        this.request = request;
        this.response = response;
        this.attemptNumber = attemptNumber;
        this.exception = exception;
        this.timestamp = Instant.now();
    }

    public String getServiceName() {
        return serviceName;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public Exception getException() {
        return exception;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}

