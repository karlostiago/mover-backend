package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.BrandEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BrandTest {

    private Brand brand;
    private Symbol mockSymbol;

    @BeforeEach
    void setUp() {
        brand = new Brand();
        mockSymbol = new Symbol();
        mockSymbol.setId(1L);
        mockSymbol.setDescription("image description");
        mockSymbol.setImageBase64("base64");
    }

    @Test
    void testToEntity() {
        brand.setId(1L);
        brand.setName("Brand Name");
        brand.setSymbol(mockSymbol);
        brand.setActive(true);

        BrandEntity entity = brand.toEntity();

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Brand Name".toUpperCase(), entity.getName());
        assertNotNull(entity.getSymbol());
        assertTrue(entity.getActive());
    }
}
