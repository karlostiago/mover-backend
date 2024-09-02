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
        Integer expectedState = 1;
        String expectedCep = "cep";
        LocalDate expectedBirthDate = LocalDate.of(2024, 8, 17);
        UserRequest expectedUser = new UserRequest();

        request.setId(expectedId);
        request.setName(expectedName);
        request.setCpfCnpj(expectedCpf);
        request.setRg(expectedRg);
        request.setEmail(expectedEmail);
        request.setNumber(expectedNumber);
        request.setBrazilianStateCode(1);
        request.setPostalCode(expectedCep);
        request.setBirthDate(expectedBirthDate);
        request.setUser(expectedUser);

        assertEquals(expectedId, request.getId());
        assertEquals(expectedName, request.getName());
        assertEquals(expectedCpf, request.getCpfCnpj());
        assertEquals(expectedRg, request.getRg());
        assertEquals(expectedEmail, request.getEmail());
        assertEquals(expectedNumber, request.getNumber());
        assertEquals(expectedState, request.getBrazilianStateCode().intValue());
        assertEquals(expectedCep, request.getPostalCode());
        assertEquals(expectedBirthDate, request.getBirthDate());
        assertEquals(expectedUser, request.getUser());
    }
}
