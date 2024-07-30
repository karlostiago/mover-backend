package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.repository.BrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class BrandServiceTest {

    @Autowired
    private BrandService brandService;

    @MockBean
    private BrandRepository brandRepository;

    @MockBean
    private ImageIOService imageIOService;

    @Mock
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAnErroWhenUploadFails() {
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", new byte[10]);

        var thrown = assertThrows(NotificationException.class, () -> brandService.upload(file));

        assertEquals("Arquivo não é uma imagem válida.", thrown.getMessage());
    }

    @Test
    void shouldReturnAnErrorWhenFileExtesionIsNotPng() {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpeg", "image/png", new byte[10]);

        var thrown = assertThrows(NotificationException.class, () -> brandService.upload(file));

        assertEquals("O arquivo deve ter a extensão .png", thrown.getMessage());
    }

    @Test
    void shouldReturnAnErrorWhenFileIsNull() {
        MockMultipartFile file = new MockMultipartFile("file", null, "image/png", new byte[10]);

        var thrown = assertThrows(NotificationException.class, () -> brandService.upload(file));

        assertEquals("Nenhum arquivo carregado.", thrown.getMessage());
    }

    @Test
    void shouldReturnAnErrorWhenUnableToReadBytesOfAnImage() throws IOException {
        InputStream content = getClass().getClassLoader().getResourceAsStream("upload/caoa.png");

        when(file.getOriginalFilename()).thenReturn("caoa.png");
        when(file.getContentType()).thenReturn("image/png");
        when(file.getInputStream()).thenReturn(content);

        when(imageIOService.read(any(InputStream.class))).thenThrow(new IOException());

        var thrown = assertThrows(NotificationException.class, () -> brandService.upload(file));

        assertNotNull(thrown);
    }

    @Test
    void shouldReturnSucessWhenFileUpload() throws IOException {
        InputStream content = getClass().getClassLoader().getResourceAsStream("upload/caoa.png");

        when(file.getOriginalFilename()).thenReturn("caoa.png");
        when(file.getContentType()).thenReturn("image/png");
        when(file.getInputStream()).thenReturn(content);

        BufferedImage mockImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        when(imageIOService.read(any(InputStream.class))).thenReturn(mockImage);

        BufferedImage result = brandService.upload(file);

        assertNotNull(result);
        assertEquals(mockImage.getWidth(), result.getWidth());
        assertEquals(mockImage.getHeight(), result.getHeight());
    }

    @Test
    void shouldReturnBase64WhenFileIsValid() {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        String result = brandService.toBase64(image);

        assertNotNull(result);
    }

    @Test
    void shouldThrowNotificationExceptionWhenImageIOWriteFails() throws IOException {
        BufferedImage mockImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        doThrow(new IOException("Erro de escrita")).when(imageIOService).write(eq(mockImage), eq("png"), any(ByteArrayOutputStream.class));

        var thrown = assertThrows(NotificationException.class, () -> brandService.toBase64(mockImage));

        assertEquals("Erro de escrita", thrown.getMessage());
    }

    @Test
    void shouldFilterByName() {
        List<BrandEntity> brands = List.of(new BrandEntity(), new BrandEntity());
        when(brandRepository.findByNameContainingIgnoreCase("test")).thenReturn(brands);

        List<BrandEntity> result = brandService.filterByName("test");

        assertEquals(brands, result);
        verify(brandRepository, times(1)).findByNameContainingIgnoreCase("test");
    }
}

