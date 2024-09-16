package com.ctsousa.mover.response;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClientResponseTest {

    @Test
    void testResponseSettersAndGetters() {
        ClientResponse response = new ClientResponse();
        Long expectedId = 1L;
        String expectedName = "Client Name";
        String expectedRg = "123456789";
        String expectedState = "CE";
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

        ContactResponse contactResponse = new ContactResponse();
        contactResponse.setClientId(1L);
        contactResponse.setId(1L);

        response.setId(expectedId);
        response.setName(expectedName);
        response.setRg(expectedRg);
        response.setState(expectedState);
        response.setPostalCode(expectedCep);
        response.setCpfCnpj(expectedCpf);
        response.setEmail(expectedEmail);
        response.setNumber(expectedNumber);
        response.setUser(expectedUser);
        response.setContacts(new ArrayList<>());
        response.getContacts().add(contactResponse);

        assertEquals(expectedId, response.getId());
        assertEquals(expectedName, response.getName());
        assertEquals(expectedRg, response.getRg());
        assertEquals(expectedState, response.getUf());
        assertEquals(expectedCep, response.getPostalCode());
        assertEquals(expectedCpf, response.getCpfCnpj());
        assertEquals(expectedEmail, response.getEmail());
        assertEquals(expectedNumber, response.getNumber());
        assertEquals(1, response.getContacts().size());

        UserResponse actualUser = response.getUser();

        assertNotNull(actualUser);
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getLogin(), actualUser.getLogin());
        assertEquals(expectedUser.getClientId(), actualUser.getClientId());
    }
}
