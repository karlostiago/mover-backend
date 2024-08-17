package com.ctsousa.mover.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HealthCheckResponseTest {

    @Test
    void testResponseSettersAndGetters() {
        HealthCheckResponse response = new HealthCheckResponse(true);

        Assertions.assertTrue(response.isRunning());
    }
}
