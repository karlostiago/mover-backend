package com.ctsousa.mover.service;

import com.ctsousa.mover.service.impl.ImageIOServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageIOServiceTest {

    @InjectMocks
    private ImageIOServiceImpl imageIOService;

    private MockedStatic<ImageIO> mockedImageIO;

    @BeforeEach
    public void setUp() {
        mockedImageIO = mockStatic(ImageIO.class);
    }

    @AfterEach
    public void after() {
        mockedImageIO.close();
    }

    @Test
    void mustReadImage() throws IOException {
        InputStream mockInputStream = mock(InputStream.class);
        BufferedImage mockBufferedImage = mock(BufferedImage.class);

        when(ImageIO.read(mockInputStream)).thenReturn(mockBufferedImage);

        BufferedImage result = imageIOService.read(mockInputStream);

        assertNotNull(result);
        assertEquals(mockBufferedImage, result);
        mockedImageIO.verify(() -> ImageIO.read(mockInputStream), times(1));
    }

    @Test
    void shouldReturnAnErrorWhenRead() throws IOException {
        InputStream mockInputStream = mock(InputStream.class);

        when(ImageIO.read(mockInputStream)).thenThrow(new IOException("IO error"));

        IOException exception = assertThrows(IOException.class, () -> imageIOService.read(mockInputStream));

        assertEquals("IO error", exception.getMessage());
        mockedImageIO.verify(() -> ImageIO.read(mockInputStream), times(1));
    }

    @Test
    void mustWriteImage() throws IOException {
        BufferedImage mockBufferedImage = mock(BufferedImage.class);
        ByteArrayOutputStream mockByteArrayOutputStream = mock(ByteArrayOutputStream.class);
        String extension = "png";

        when(ImageIO.write(mockBufferedImage, extension, mockByteArrayOutputStream)).thenReturn(true);

        imageIOService.write(mockBufferedImage, extension, mockByteArrayOutputStream);

        mockedImageIO.verify(() -> ImageIO.write(mockBufferedImage, extension, mockByteArrayOutputStream), times(1));
    }

    @Test
    void shouldReturnAnErrorWhenWrite() throws IOException {
        BufferedImage mockBufferedImage = mock(BufferedImage.class);
        ByteArrayOutputStream mockByteArrayOutputStream = mock(ByteArrayOutputStream.class);
        String extension = "png";

        when(ImageIO.write(mockBufferedImage, extension, mockByteArrayOutputStream)).thenThrow(new IOException("IO error"));

        IOException exception = assertThrows(IOException.class, () -> imageIOService.write(mockBufferedImage, extension, mockByteArrayOutputStream));

        assertEquals("IO error", exception.getMessage());
        mockedImageIO.verify(() -> ImageIO.write(mockBufferedImage, extension, mockByteArrayOutputStream), times(1));

    }
}
