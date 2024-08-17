package com.ctsousa.mover.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserResponseTest {

    @Test
    void testResponseSettersAndGetters() {
        UserResponse response = new UserResponse();
        Long expectedId = 1L;
        String expectedName = "John Doe";
        String expectedEmail = "john.doe@example.com";
        String expectedLogin = "johndoe";
        Long expectedClientId = 100L;

        response.setId(expectedId);
        response.setName(expectedName);
        response.setEmail(expectedEmail);
        response.setLogin(expectedLogin);
        response.setClientId(expectedClientId);

        assertEquals(expectedId, response.getId());
        assertEquals(expectedName, response.getName());
        assertEquals(expectedEmail, response.getEmail());
        assertEquals(expectedLogin, response.getLogin());
        assertEquals(expectedClientId, response.getClientId());
    }
}
