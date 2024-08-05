package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.SymbolEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.repository.SymbolRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
/*
@SpringBootTest
@ActiveProfiles("test")
public class SymbolServiceTest {

    @Autowired
    private SymbolService symbolService;

    @Autowired
    private SymbolRepository symbolRepository;

    @AfterEach
    void after() {
//        symbolRepository.deleteAll();
    }

    @Test
    void mustDeleteById() {
        SymbolEntity entity = new SymbolEntity();
        entity.setImageBase64("laksdjlaksjdla====ae=a==+S+S+");
        entity.setDescription("Test Symbol");

        SymbolEntity savedSymbol = symbolRepository.save(entity);

        assertNotNull(savedSymbol.getId());
        assertTrue(savedSymbol.getActive());

        symbolService.deleteById(savedSymbol.getId());

        assertEquals(0, symbolRepository.findAll().size());
    }

    @Test
    void shouldNotDeleteSymbolWhenIsAlreadyPredefined() {
        SymbolEntity entity = new SymbolEntity();
        entity.setImageBase64("laksdjlaksjdla====ae=a==+S+S+");
        entity.setDescription("Fiat");

        SymbolEntity savedSymbol = symbolRepository.save(entity);

        assertNotNull(savedSymbol.getId());
        assertTrue(savedSymbol.getActive());

        symbolService.deleteById(savedSymbol.getId());

        assertEquals(1, symbolRepository.findAll().size());
    }

    @Test
    void mustSavedSymbol() {
        SymbolEntity entity = new SymbolEntity();
        entity.setImageBase64("laksdjlaksjdla====ae=a==+S+S+");
        entity.setDescription("BMW");

        SymbolEntity savedSymbol = symbolService.save(entity);

        assertNotNull(savedSymbol);
        assertNotNull(savedSymbol.getId());
        assertEquals(entity.getDescription(), savedSymbol.getDescription());
    }

    @Test
    void shouldReturnAnErrorWhenExistsSymbolWithSameName() {
        symbolService.save(new SymbolEntity("BMW", "laksdjlaksjdla====ae=a==+S+S+"));

        assertEquals(1, symbolRepository.findAll().size());

        try {
            symbolService.save(new SymbolEntity("BMW", "laksdjlaksjdla====ae=a==+S+S+"));
            fail();
        } catch (NotificationException e) {
            assertEquals("Já existe um simbolo com o nome informado.", e.getMessage());
        }
    }
}

 */
