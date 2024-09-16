package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testToEntity() {
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setLogin("johndoe");
        user.setPassword("password123");
        user.setClientId(1L);

        UserEntity entity = user.toEntity();

        assertNotNull(entity);
        assertEquals("JOHN DOE", entity.getName());
        assertEquals("JOHN.DOE@EXAMPLE.COM", entity.getEmail()); 
        assertEquals("johndoe", entity.getLogin());
        assertEquals("password123", entity.getPassword());
        assertEquals(1L, entity.getClientId());
    }
}
