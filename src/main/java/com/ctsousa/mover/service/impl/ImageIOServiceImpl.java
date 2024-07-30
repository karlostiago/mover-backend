package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.service.ImageIOService;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageIOServiceImpl implements ImageIOService {
    @Override
    public BufferedImage read(InputStream inputStream) throws IOException {
        return ImageIO.read(inputStream);
    }

    @Override
    public void write(BufferedImage image, String extension, ByteArrayOutputStream bytes) throws IOException {
        ImageIO.write(image, extension, bytes);
    }
}
