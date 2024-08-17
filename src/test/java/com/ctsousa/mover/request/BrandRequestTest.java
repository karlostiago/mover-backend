package com.ctsousa.mover.request;

import com.ctsousa.mover.domain.Symbol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BrandRequestTest {

    @Test
    void shouldRequestSettersAndGetters() {
        BrandRequest request = new BrandRequest();
        Long expectedId = 1L;
        String expectedName = "BrandName";
        Symbol expectedSymbol = new Symbol();
        Boolean expectedActive = true;

        request.setId(expectedId);
        request.setName(expectedName);
        request.setSymbol(expectedSymbol);
        request.setActive(expectedActive);

        assertEquals(expectedId, request.getId());
        assertEquals(expectedName, request.getName());
        assertEquals(expectedSymbol, request.getSymbol());
        assertEquals(expectedActive, request.getActive());
    }
}
