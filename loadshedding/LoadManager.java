package com.example.microservices.loadshedding;

import com.example.microservices.model.Request;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple load manager that decides if the system can handle a request.
 * Could be expanded to track system metrics, implement priority queues, etc.
 */
public class LoadManager {
    private final int maxConcurrentRequests;
    private final AtomicInteger currentRequests = new AtomicInteger(0);

    public LoadManager(int maxConcurrentRequests) {
        this.maxConcurrentRequests = maxConcurrentRequests;
    }

    public boolean acceptRequest(Request request) {
        int curr = currentRequests.incrementAndGet();
        if (curr > maxConcurrentRequests) {
            currentRequests.decrementAndGet();
            return false;
        }
        return true;
    }

    public void releaseRequest() {
        currentRequests.decrementAndGet();
    }
}

