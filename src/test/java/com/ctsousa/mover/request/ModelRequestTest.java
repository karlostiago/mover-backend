package com.ctsousa.mover.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelRequestTest {

    @Test
    void shouldRequestSettersAndGetters() {
        ModelRequest request = new ModelRequest();
        Long expectedId = 1L;
        String expectedName = "name";
        Long expectedBrandId = 2L;
        Boolean expectedActive = true;

        request.setId(expectedId);
        request.setBrandId(expectedBrandId);
        request.setActive(expectedActive);
        request.setName(expectedName);

        assertEquals(expectedId, request.getId());
        assertEquals(expectedName, request.getName());
        assertEquals(expectedBrandId, request.getBrandId());
        assertEquals(expectedActive, request.getActive());
    }
}
