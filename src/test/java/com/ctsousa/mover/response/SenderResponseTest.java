package com.ctsousa.mover.response;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SenderResponseTest {

    @Test
    void testResponseSettersAndGetters() {
        SenderResponse senderResponse = new SenderResponse();
        Long expectedId = 1L;
        Long expectedClientId = 2L;
        String expectedEmail = "sender@example.com";
        String expectedCode = "ABC123";
        LocalDateTime expectedExpiryDate = LocalDateTime.of(2024, 8, 17, 15, 30);

        senderResponse.setId(expectedId);
        senderResponse.setClientId(expectedClientId);
        senderResponse.setEmail(expectedEmail);
        senderResponse.setCode(expectedCode);
        senderResponse.setExpiryDate(expectedExpiryDate);

        assertEquals(expectedId, senderResponse.getId());
        assertEquals(expectedClientId, senderResponse.getClientId());
        assertEquals(expectedEmail, senderResponse.getEmail());
        assertEquals(expectedCode, senderResponse.getCode());
        assertEquals(expectedExpiryDate, senderResponse.getExpiryDate());
    }
}
