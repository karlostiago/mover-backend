package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.entity.SymbolEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.repository.BrandRepository;
import org.junit.jupiter.api.Assertions;
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

        doThrow(new IOException()).when(imageIOService).write(eq(mockImage), eq("png"), any(ByteArrayOutputStream.class));

        var thrown = assertThrows(NotificationException.class, () -> brandService.toBase64(mockImage));

        assertNotNull(thrown);
    }

    @Test
    void shouldFilterByName() {
        List<BrandEntity> brands = List.of(new BrandEntity(), new BrandEntity());
        when(brandRepository.findByNameContainingIgnoreCase("test")).thenReturn(brands);

        List<BrandEntity> result = brandService.filterByName("test");

        assertEquals(brands, result);
        verify(brandRepository, times(1)).findByNameContainingIgnoreCase("test");
    }

    @Test
    void shouldReturnAnErrorWhenSavingBrandWithSameName() {
        when(brandRepository.existsByName(any())).thenReturn(true);

        var thrown = Assertions.assertThrows(NotificationException.class, () -> brandService.save(new BrandEntity()));

        assertEquals("Existe uma marca, cadastrada com o nome informado.", thrown.getMessage());
    }

    @Test
    void shouldReturnAnErrorWhenUpdatingBrandWithSameName() {
        BrandEntity entity = new BrandEntity();
        entity.setId(1L);

        when(brandRepository.existsByNameNotId(any(), any())).thenReturn(true);

        var thrown = Assertions.assertThrows(NotificationException.class, () -> brandService.save(entity));

        assertEquals("Não foi possível atualizar, pois já tem uma marca, com o nome informado.", thrown.getMessage());
    }

    @Test
    void mustSaveBrand() {
        BrandEntity newEntity = new BrandEntity();
        when(brandRepository.save(newEntity)).thenReturn(getEntity());

        var result = brandService.save(newEntity);

        assertNotNull(result.getId());
        assertNotNull(result.getSymbol().getId());
    }

    @Test
    void mustUpateBrand() {
        BrandEntity entity = getEntity();

        entity.setActive(false);
        entity.getSymbol().setActive(false);

        when(brandRepository.save(entity)).thenReturn(entity);

        assertFalse(entity.getActive());
        assertFalse(entity.getSymbol().getActive());
    }

    private BrandEntity getEntity() {
        BrandEntity entity = new BrandEntity();
        entity.setId(1L);
        entity.setActive(true);
        entity.setName("Brand test");

        SymbolEntity symbol = new SymbolEntity();
        symbol.setId(1L);
        symbol.setDescription("Symbol teste");
        symbol.setImageBase64("lakwehpaoidnalmcamnek====");
        symbol.setActive(true);
        entity.setSymbol(symbol);
        return entity;
    }
}

