package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.entity.ContactEntity;
import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientTest {

    private Client client;
    private Contact mockContact;
    private User mockUser;

    @BeforeEach
    void setUp() {
        client = new Client();
        mockContact = mock(Contact.class);
        mockUser = mock(User.class);
    }

    @Test
    void testToEntity() {
        client.setId(1L);
        client.setName("John Doe");
        client.setCpfCnpj("12345678909");
        client.setCellPhone("11987654321");
        client.setTelephone("1134567890");
        client.setPostalCode("12345678");
        client.setBrazilianStateCode(1);
        client.setTypePersonCode(1);
        client.setContacts(List.of(mockContact));
        client.setUser(mockUser);
        client.setActive(true);
        client.setEmail("teste@gmail.com");
        client.setBirthDate(LocalDate.of(1990, 1, 1));

        when(mockContact.toEntity()).thenReturn(new ContactEntity());
        when(mockUser.toEntity()).thenReturn(new UserEntity());

        ClientEntity entity = client.toEntity();

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("JOHN DOE", entity.getName());
        assertEquals("12345678909", entity.getCpfCnpj());
        assertEquals("11987654321", entity.getCellPhone());
        assertEquals("1134567890", entity.getTelephone());
        assertEquals("12345678", entity.getPostalCode());
        assertTrue(entity.getActive());
    }

    @Test
    void testToEntityInvalidCpfShouldThrowException() {
        client.setCpfCnpj("invalidCpf");

        assertThrows(NotificationException.class, client::toEntity);
    }

    @Test
    void testToEntityInvalidCellPhoneShouldThrowException() {
        client.setCpfCnpj("12345678909");
        client.setCellPhone("123");

        assertThrows(NotificationException.class, client::toEntity);
    }

    @Test
    void testToEntity_InvalidTelephone_ShouldThrowException() {
        client.setCpfCnpj("12345678909");
        client.setCellPhone("11987654321");
        client.setTelephone("12345");

        assertThrows(NotificationException.class, client::toEntity);
    }

    @Test
    void testToEntityInvalidPostalCodeShouldThrowException() {
        client.setCpfCnpj("12345678909");
        client.setCellPhone("11987654321");
        client.setPostalCode("123");

        assertThrows(NotificationException.class, client::toEntity);
    }
}
