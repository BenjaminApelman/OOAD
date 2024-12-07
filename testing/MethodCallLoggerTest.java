package com.example.microservices.logging;

import com.example.microservices.model.Request;
import com.example.microservices.model.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MethodCallLoggerTest {
    @Test
    void testLogging() {
        MethodCallLogger logger = new MethodCallLogger();
        Request req = new Request("OpName", "param=value");
        Response res = new Response("OK", 200);

        logger.logCall("TestService", req, res, 1);
        assertEquals(1, logger.getRecords().size());
        ServiceCallRecord record = logger.getRecords().get(0);
        assertEquals("TestService", record.getServiceName());
        assertEquals(req, record.getRequest());
        assertEquals(res, record.getResponse());
        assertNull(record.getException());
        assertTrue(record.getTimestamp() != null);
    }

    @Test
    void testLoggingException() {
        MethodCallLogger logger = new MethodCallLogger();
        Request req = new Request("OpName", "param=value");
        RuntimeException ex = new RuntimeException("Error");

        logger.logCall("TestService", req, null, 1, ex);
        ServiceCallRecord record = logger.getRecords().get(0);
        assertEquals(ex, record.getException());
        assertNull(record.getResponse());
    }
}
