package com.example.microservices.loadshedding;

import com.example.microservices.model.Request;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoadManagerTest {
    @Test
    void testLoadAcceptance() {
        LoadManager manager = new LoadManager(2);
        Request req = new Request("Op", "param");

        assertTrue(manager.acceptRequest(req));
        assertTrue(manager.acceptRequest(req));
        // Third should fail
        assertFalse(manager.acceptRequest(req));
    }

    @Test
    void testLoadRelease() {
        LoadManager manager = new LoadManager(1);
        Request req = new Request("Op", "param");

        assertTrue(manager.acceptRequest(req));
        assertFalse(manager.acceptRequest(req));

        manager.releaseRequest();
        assertTrue(manager.acceptRequest(req));
    }
}
