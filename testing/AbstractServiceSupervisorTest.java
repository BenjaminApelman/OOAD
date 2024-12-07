package com.example.microservices.supervisor;

import com.example.microservices.logging.MethodCallLogger;
import com.example.microservices.loadshedding.LoadManager;
import com.example.microservices.model.Request;
import com.example.microservices.model.Response;
import com.example.microservices.retry.RetryPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AbstractServiceSupervisorTest {
    private AbstractServiceSupervisor supervisor;
    private MethodCallLogger logger;
    private LoadManager loadManager;
    private RetryPolicy retryPolicy;

    @BeforeEach
    void setup() {
        logger = new MethodCallLogger();
        loadManager = mock(LoadManager.class);
        retryPolicy = mock(RetryPolicy.class);
        when(loadManager.acceptRequest(any())).thenReturn(true);
        
        supervisor = new AbstractServiceSupervisor(logger, retryPolicy, loadManager) {
            @Override
            protected Response processRequest(Request request) throws Exception {
                if ("fail".equals(request.getOperationName())) {
                    throw new RuntimeException("Simulated failure");
                }
                return new Response("Success", 200);
            }

            @Override
            protected String getServiceName() {
                return "TestService";
            }
        };
    }

    @Test
    void testSuccessfulRequest() {
        when(retryPolicy.shouldRetry(anyInt())).thenReturn(false);
        Request request = new Request("ok", "param=value");
        Response response = supervisor.handleRequest(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        // Check log
        assertEquals(1, logger.getRecords().size());
        assertNull(logger.getRecords().get(0).getException());
    }

    @Test
    void testFailedRequestWithRetry() {
        // first attempt fails, second attempt succeeds
        when(retryPolicy.shouldRetry(1)).thenReturn(true);
        when(retryPolicy.shouldRetry(2)).thenReturn(false);
        when(retryPolicy.getWaitTimeMs(1)).thenReturn(50L);

        Request request = new Request("fail", "trigger=error");
        Response response = supervisor.handleRequest(request);

        // The second attempt also fails, ultimately no success
        assertEquals(500, response.getStatusCode());
        // Check logs - should have two attempts logged
        assertEquals(1, logger.getRecords().size());
        assertEquals("TestService", logger.getRecords().get(0).getServiceName());
        assertNotNull(logger.getRecords().get(0).getException());
    }

    @Test
    void testLoadShedding() {
        when(loadManager.acceptRequest(any())).thenReturn(false);

        Request request = new Request("any", "params");
        Response response = supervisor.handleRequest(request);

        assertEquals(503, response.getStatusCode());
        assertTrue(logger.getRecords().isEmpty());
    }
}
