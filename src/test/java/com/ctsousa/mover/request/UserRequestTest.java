package com.ctsousa.mover.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserRequestTest {

    @Test
    void shouldRequestSettersAndGetters() {
        UserRequest request = new UserRequest();
        Long expectedId = 1L;
        String expectedName = "name";
        String expectedEmail = "email";
        String expectedLogin = "login";
        String expectedPassword = "password";
        Long expectedClientId = 2L;

        request.setId(expectedId);
        request.setName(expectedName);
        request.setEmail(expectedEmail);
        request.setLogin(expectedLogin);
        request.setPassword(expectedPassword);
        request.setClientId(expectedClientId);

        assertEquals(expectedId, request.getId());
        assertEquals(expectedName, request.getName());
        assertEquals(expectedEmail, request.getEmail());
        assertEquals(expectedLogin, request.getLogin());
        assertEquals(expectedPassword, request.getPassword());
        assertEquals(expectedClientId, request.getClientId());
    }
}
