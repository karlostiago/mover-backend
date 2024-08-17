package com.ctsousa.mover.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelResponseTest {

    @Test
    void testResponseSettersAndGetters() {
        ModelResponse response = new ModelResponse();
        Long expectedId = 1L;
        String expectedName = "Model Name";
        Long expectedBrandId = 10L;
        String expectedBrandName = "Brand Name";
        Boolean expectedActive = true;

        response.setId(expectedId);
        response.setName(expectedName);
        response.setBrandId(expectedBrandId);
        response.setBrandName(expectedBrandName);
        response.setActive(expectedActive);

        assertEquals(expectedId, response.getId());
        assertEquals(expectedName, response.getName());
        assertEquals(expectedBrandId, response.getBrandId());
        assertEquals(expectedBrandName, response.getBrandName());
        assertEquals(expectedActive, response.getActive());
    }
}
