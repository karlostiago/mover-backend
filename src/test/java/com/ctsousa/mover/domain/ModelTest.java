package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.entity.ModelEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModelTest {

    private Model model;
    private Brand mockBrand;

    @BeforeEach
    void setUp() {
        model = new Model();
        mockBrand = mock(Brand.class);
    }

    @Test
    void testToEntity() {
        model.setId(1L);
        model.setName("Car Model");
        model.setActive(true);

        BrandEntity mockBrandEntity = new BrandEntity();
        mockBrandEntity.setId(10L);

        when(mockBrand.getId()).thenReturn(10L);
        model.setBrand(mockBrand);

        ModelEntity entity = model.toEntity();

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("CAR MODEL", entity.getName());
        assertTrue(entity.getActive());

        assertNotNull(entity.getBrand());
        assertEquals(10L, entity.getBrand().getId());
    }
}
