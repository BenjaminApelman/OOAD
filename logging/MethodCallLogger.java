package com.example.microservices.logging;

import com.example.microservices.model.Request;
import com.example.microservices.model.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps a record of method calls, their parameters, timestamps, and outcomes.
 * Useful for diagnosing metastable failures.
 */
public class MethodCallLogger {
    private final List<ServiceCallRecord> records = new ArrayList<>();

    public synchronized void logCall(String serviceName, Request request, Response response, int attempt) {
        records.add(new ServiceCallRecord(serviceName, request, response, attempt));
    }

    public synchronized void logCall(String serviceName, Request request, Response response, int attempt, Exception ex) {
        records.add(new ServiceCallRecord(serviceName, request, response, attempt, ex));
    }

    public synchronized List<ServiceCallRecord> getRecords() {
        return new ArrayList<>(records);
    }
}

