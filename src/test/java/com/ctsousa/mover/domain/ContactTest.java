package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.entity.ContactEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContactTest {

    private Contact contact;
    private ClientEntity mockClientEntity;

    @BeforeEach
    void setUp() {
        contact = new Contact();
        mockClientEntity = mock(ClientEntity.class);
    }

    @Test
    void testToEntity() {
        contact.setId(1L);
        contact.setName("John Doe");
        contact.setTelephone("1134567890");
        contact.setDegreeKinship("Friend");
        contact.setClient(mockClientEntity);

        when(mockClientEntity.getActive()).thenReturn(true);

        ContactEntity entity = contact.toEntity();

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("JOHN DOE", entity.getName());
        assertEquals("1134567890", entity.getTelephone());
        assertEquals(mockClientEntity, entity.getClient());
        assertEquals("FRIEND", entity.getDegreeKinship());
        assertTrue(entity.getActive());
    }

    @Test
    void testToEntityInvalidTelephoneShouldThrowException() {
        contact.setTelephone("12345");

        NotificationException exception = assertThrows(NotificationException.class, contact::toEntity);
        assertEquals("Por gentileza informar um número de telefone válido para contato de referência.", exception.getMessage());
    }

    @Test
    void testToEntityNullTelephoneShouldNotThrowException() {
        contact.setId(1L);
        contact.setName("John Doe");
        contact.setDegreeKinship("Friend");
        contact.setClient(mockClientEntity);

        when(mockClientEntity.getActive()).thenReturn(true);

        ContactEntity entity = contact.toEntity();

        assertNotNull(entity);
        assertNull(entity.getTelephone());
    }

    @Test
    void testToEntityEmptyTelephoneShouldNotThrowException() {
        contact.setId(1L);
        contact.setName("John Doe");
        contact.setTelephone("");
        contact.setDegreeKinship("Friend");
        contact.setClient(mockClientEntity);

        when(mockClientEntity.getActive()).thenReturn(true);

        ContactEntity entity = contact.toEntity();

        assertNotNull(entity);
        assertEquals("", entity.getTelephone());
    }
}
