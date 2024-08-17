package com.ctsousa.mover.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClientResponseTest {

    @Test
    void testResponseSettersAndGetters() {
        ClientResponse response = new ClientResponse();
        Long expectedId = 1L;
        String expectedName = "Client Name";
        String expectedRg = "123456789";
        String expectedState = "State";
        String expectedCep = "12345-678";
        String expectedCpf = "98765432100";
        String expectedEmail = "client@example.com";
        String expectedNumber = "1234";

        UserResponse expectedUser = new UserResponse();
        expectedUser.setId(2L);
        expectedUser.setName("User Name");
        expectedUser.setEmail("user@example.com");
        expectedUser.setLogin("userlogin");
        expectedUser.setClientId(3L);

        response.setId(expectedId);
        response.setName(expectedName);
        response.setRg(expectedRg);
        response.setState(expectedState);
        response.setCep(expectedCep);
        response.setCpf(expectedCpf);
        response.setEmail(expectedEmail);
        response.setNumber(expectedNumber);
        response.setUser(expectedUser);

        assertEquals(expectedId, response.getId());
        assertEquals(expectedName, response.getName());
        assertEquals(expectedRg, response.getRg());
        assertEquals(expectedState, response.getState());
        assertEquals(expectedCep, response.getCep());
        assertEquals(expectedCpf, response.getCpf());
        assertEquals(expectedEmail, response.getEmail());
        assertEquals(expectedNumber, response.getNumber());

        UserResponse actualUser = response.getUser();

        assertNotNull(actualUser);
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getLogin(), actualUser.getLogin());
        assertEquals(expectedUser.getClientId(), actualUser.getClientId());
    }
}
