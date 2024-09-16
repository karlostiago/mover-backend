package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.SenderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SenderTest {

    private Sender sender;

    @BeforeEach
    void setUp() {
        sender = new Sender();
    }

    @Test
    void testToEntity() {
        sender.setId(1L);
        sender.setEmail("test@example.com");
        sender.setCode("ABC123");
        sender.setExpiryDate(LocalDateTime.of(2024, 9, 15, 12, 30));

        SenderEntity entity = sender.toEntity();

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("test@example.com", entity.getEmail());
        assertEquals("ABC123", entity.getCode());
        assertEquals(LocalDateTime.of(2024, 9, 15, 12, 30), entity.getExpiryDate());
        assertTrue(entity.getActive());
    }
}
