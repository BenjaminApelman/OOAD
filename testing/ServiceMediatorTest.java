package com.example.microservices.mediator;

import com.example.microservices.logging.MethodCallLogger;
import com.example.microservices.model.Request;
import com.example.microservices.model.Response;
import com.example.microservices.model.ServiceIdentifier;
import com.example.microservices.services.BaseService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServiceMediatorTest {
    @Test
    void testDispatchRequest() {
        MethodCallLogger logger = new MethodCallLogger();
        ServiceMediator mediator = new ServiceMediator(logger);

        BaseService mockService = mock(BaseService.class);
        when(mockService.getIdentifier()).thenReturn(ServiceIdentifier.ORDER);
        when(mockService.handleRequest(any())).thenReturn(new Response("OK", 200));

        mediator.registerService(mockService);

        Request req = new Request("Op", "param");
        Response res = mediator.dispatchRequest(ServiceIdentifier.ORDER, req);
        assertEquals(200, res.getStatusCode());
        verify(mockService, times(1)).handleRequest(eq(req));
    }

    @Test
    void testUnknownService() {
        MethodCallLogger logger = new MethodCallLogger();
        ServiceMediator mediator = new ServiceMediator(logger);

        Request req = new Request("Op", "param");
        Response res = mediator.dispatchRequest(ServiceIdentifier.ORDER, req);
        assertEquals(404, res.getStatusCode());
    }
}
