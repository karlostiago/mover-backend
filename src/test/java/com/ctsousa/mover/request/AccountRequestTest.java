package com.ctsousa.mover.request;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountRequestTest {

    @Test
    void shouldRequestSettersAndGetters() {
        AccountRequest request = new AccountRequest();
        Long expectedId = 1L;
        String expectedName = "AccountName";
        String expectedNumber = "123";
        Integer expectedCodeIcon = 1;
        BigDecimal expectedInitialBalance = BigDecimal.valueOf(1000D);
        Boolean expectedCaution = false;
        Boolean expectedActive = true;


        request.setId(expectedId);
        request.setName(expectedName);
        request.setNumber(expectedNumber);
        request.setCodeIcon(expectedCodeIcon);
        request.setCaution(expectedCaution);
        request.setActive(expectedActive);
        request.setInitialBalance(expectedInitialBalance);

        assertEquals(expectedId, request.getId());
        assertEquals(expectedName, request.getName());
        assertEquals(expectedNumber, request.getNumber());
        assertEquals(expectedCodeIcon, request.getCodeIcon());
        assertEquals(expectedInitialBalance, request.getInitialBalance());
        assertEquals(expectedCaution, request.getCaution());
        assertEquals(expectedActive, request.getActive());
    }
}
