package com.example.microservices.retry;

public class ExponentialBackoffRetryPolicy implements RetryPolicy {
    private final int maxAttempts;
    private final long baseDelayMs;

    public ExponentialBackoffRetryPolicy(int maxAttempts, long baseDelayMs) {
        this.maxAttempts = maxAttempts;
        this.baseDelayMs = baseDelayMs;
    }

    @Override
    public boolean shouldRetry(int attemptNumber) {
        return attemptNumber < maxAttempts;
    }

    @Override
    public long getWaitTimeMs(int attemptNumber) {
        return (long) (baseDelayMs * Math.pow(2, attemptNumber - 1));
    }
}
