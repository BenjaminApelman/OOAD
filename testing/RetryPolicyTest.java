package com.example.microservices.retry;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RetryPolicyTest {
    @Test
    void testExponentialBackoffRetryPolicy() {
        RetryPolicy policy = new ExponentialBackoffRetryPolicy(3, 100);

        assertTrue(policy.shouldRetry(1));
        assertTrue(policy.shouldRetry(2));
        assertFalse(policy.shouldRetry(3)); // 3 attempts means we've tried enough

        long waitTimeFirstAttempt = policy.getWaitTimeMs(1);
        long waitTimeSecondAttempt = policy.getWaitTimeMs(2);

        assertEquals(100, waitTimeFirstAttempt);
        assertEquals(200, waitTimeSecondAttempt); // doubled
    }
}

