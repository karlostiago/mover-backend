package com.ctsousa.mover.response;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountResponseTest {

    @Test
    void testResponseSettersAndGetters() {
        AccountResponse response = new AccountResponse();

        Long expectedId = 1L;
        String expectedName = "AccountName";
        String expectedNumber = "123";
        String expectedIcon = "BANK_WILL";
        String expectedImageIcon = "IMAGE_ICON";
        Integer expectedCodeIcon = 1;
        BigDecimal expectedInitialBalance = BigDecimal.valueOf(1000D);
        Boolean expectedCaution = false;
        Boolean expectedActive = true;

        response.setId(expectedId);
        response.setIcon(expectedIcon);
        response.setImageIcon(expectedImageIcon);
        response.setName(expectedName);
        response.setNumber(expectedNumber);
        response.setCodeIcon(expectedCodeIcon);
        response.setCaution(expectedCaution);
        response.setActive(expectedActive);
        response.setInitialBalance(expectedInitialBalance);

        assertEquals(expectedId, response.getId());
        assertEquals(expectedName, response.getName());
        assertEquals(expectedNumber, response.getNumber());
        assertEquals(expectedCodeIcon, response.getCodeIcon());
        assertEquals(expectedImageIcon, response.getImageIcon());
        assertEquals(expectedIcon, response.getIcon());
        assertEquals(expectedInitialBalance, response.getInitialBalance());
        assertEquals(expectedCaution, response.getCaution());
        assertEquals(expectedActive, response.getActive());
    }
}
