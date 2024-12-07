package com.example.microservices.retry;

public interface RetryPolicy {
    boolean shouldRetry(int attemptNumber);
    long getWaitTimeMs(int attemptNumber);
}
