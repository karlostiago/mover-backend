package com.ctsousa.mover.request;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientRequestTest {

    @Test
    void shouldRequestSettersAndGetters() {
        ClientRequest request = new ClientRequest();
        Long expectedId = 1L;
        String expectedName = "name";
        String expectedCpf = "cpf";
        String expectedRg = "rg";
        String expectedEmail = "email";
        String expectedNumber = "number";
        String expectedState = "state";
        String expectedCep = "cep";
        LocalDate expectedBirthDate = LocalDate.of(2024, 8, 17);
        UserRequest expectedUser = new UserRequest();

        request.setId(expectedId);
        request.setName(expectedName);
        request.setCpf(expectedCpf);
        request.setRg(expectedRg);
        request.setEmail(expectedEmail);
        request.setNumber(expectedNumber);
        request.setState(expectedState);
        request.setCep(expectedCep);
        request.setBirthDate(expectedBirthDate);
        request.setUser(expectedUser);

        assertEquals(expectedId, request.getId());
        assertEquals(expectedName, request.getName());
        assertEquals(expectedCpf, request.getCpf());
        assertEquals(expectedRg, request.getRg());
        assertEquals(expectedEmail, request.getEmail());
        assertEquals(expectedNumber, request.getNumber());
        assertEquals(expectedState, request.getState());
        assertEquals(expectedCep, request.getCep());
        assertEquals(expectedBirthDate, request.getBirthDate());
        assertEquals(expectedUser, request.getUser());
    }
}
