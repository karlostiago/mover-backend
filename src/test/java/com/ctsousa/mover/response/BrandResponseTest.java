package com.ctsousa.mover.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BrandResponseTest {

    @Test
    void testResponseSettersAndGetters() {
        BrandResponse response = new BrandResponse();
        Long expectedId = 1L;
        String expectedName = "Brand Name";
        Boolean expectedActive = true;

        SymbolResponse expectedSymbol = new SymbolResponse();
        expectedSymbol.setId(10L);
        expectedSymbol.setDescription("Symbol Description");
        expectedSymbol.setImageBase64("symbolImageBase64String");

        response.setId(expectedId);
        response.setName(expectedName);
        response.setSymbol(expectedSymbol);
        response.setActive(expectedActive);

        assertEquals(expectedId, response.getId());
        assertEquals(expectedName, response.getName());
        assertEquals(expectedActive, response.getActive());

        SymbolResponse actualSymbol = response.getSymbol();

        assertNotNull(actualSymbol);
        assertEquals(expectedSymbol.getId(), actualSymbol.getId());
        assertEquals(expectedSymbol.getDescription(), actualSymbol.getDescription());
        assertEquals(expectedSymbol.getImageBase64(), actualSymbol.getImageBase64());
    }
}
