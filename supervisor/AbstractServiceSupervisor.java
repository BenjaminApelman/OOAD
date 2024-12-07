package com.example.microservices.supervisor;

import com.example.microservices.logging.MethodCallLogger;
import com.example.microservices.model.Request;
import com.example.microservices.model.Response;
import com.example.microservices.retry.RetryPolicy;
import com.example.microservices.loadshedding.LoadManager;

import java.util.concurrent.atomic.AtomicReference;

/**
 * A superclass that supervises API calls to a particular service.
 * Handles retries, load shedding, and could integrate circuit breakers.
 */
public abstract class AbstractServiceSupervisor {
    protected final MethodCallLogger logger;
    protected final RetryPolicy retryPolicy;
    protected final LoadManager loadManager;
    protected AtomicReference<ServiceStatus> status = new AtomicReference<>(ServiceStatus.HEALTHY);

    public AbstractServiceSupervisor(MethodCallLogger logger, RetryPolicy retryPolicy, LoadManager loadManager) {
        this.logger = logger;
        this.retryPolicy = retryPolicy;
        this.loadManager = loadManager;
    }

    public Response handleRequest(Request request) {
        // Check if we should even accept this request (load shedding)
        if (!loadManager.acceptRequest(request)) {
            return new Response("Rejected due to load shedding", 503);
        }

        int attempts = 0;
        while (true) {
            try {
                attempts++;
                Response response = processRequest(request);
                // Log successful call
                logger.logCall(getServiceName(), request, response, attempts);
                return response;
            } catch (Exception e) {
                // If allowed by retry policy, try again
                if (retryPolicy.shouldRetry(attempts)) {
                    try {
                        Thread.sleep(retryPolicy.getWaitTimeMs(attempts));
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                } else {
                    // Log failure and return an error response
                    logger.logCall(getServiceName(), request, null, attempts, e);
                    return new Response("Failed after retries: " + e.getMessage(), 500);
                }
            }
        }
    }

    protected abstract Response processRequest(Request request) throws Exception;

    protected abstract String getServiceName();

    public ServiceStatus getStatus() {
        return status.get();
    }

    public void setStatus(ServiceStatus newStatus) {
        this.status.set(newStatus);
    }
}

