package com.ctsousa.mover.service;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.repository.BrandRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class BrandServiceTest {

    @Autowired
    private BrandService brandService;

    @MockBean
    private BrandRepository brandRepository;

    @Test
    void shouldReturnAnErroWhenUploadFails() {
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", new byte[10]);

        var thrown = Assertions.assertThrows(NotificationException.class, () -> brandService.upload(file));

        Assertions.assertEquals("Arquivo não é uma imagem válida.", thrown.getMessage());
    }

    @Test
    void shouldReturnAnErrorWhenFileExtesionIsNotPng() {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpeg", "image/png", new byte[10]);

        var thrown = Assertions.assertThrows(NotificationException.class, () -> brandService.upload(file));

        Assertions.assertEquals("O arquivo deve ter a extensão .png", thrown.getMessage());
    }

    @Test
    void shouldReturnAnErrorWhenFileIsNull() {
        MockMultipartFile file = new MockMultipartFile("file", null, "image/png", new byte[10]);

        var thrown = Assertions.assertThrows(NotificationException.class, () -> brandService.upload(file));

        Assertions.assertEquals("O arquivo deve ter a extensão .png", thrown.getMessage());
    }
}

