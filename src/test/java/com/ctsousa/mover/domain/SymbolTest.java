package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.SymbolEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SymbolTest {

    private Symbol symbol;

    @BeforeEach
    void setUp() {
        symbol = new Symbol();
    }

    @Test
    void testToEntity() {
        symbol.setId(1L);
        symbol.setDescription("Logo Symbol");
        symbol.setImageBase64("base64encodedimage");

        SymbolEntity entity = symbol.toEntity();

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("LOGO SYMBOL", entity.getDescription());
        assertEquals("base64encodedimage", entity.getImageBase64());
        assertTrue(entity.getActive());
    }
}
