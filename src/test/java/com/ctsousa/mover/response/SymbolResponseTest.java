package com.ctsousa.mover.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SymbolResponseTest {

    @Test
    void testResponseSettersAndGetters() {
        SymbolResponse response = new SymbolResponse();
        Long expectedId = 1L;
        String expectedDescription = "Symbol Description";
        String expectedImageBase64 = "imageBase64String";

        response.setId(expectedId);
        response.setDescription(expectedDescription);
        response.setImageBase64(expectedImageBase64);

        assertEquals(expectedId, response.getId());
        assertEquals(expectedDescription, response.getDescription());
        assertEquals(expectedImageBase64, response.getImageBase64());
    }
}
