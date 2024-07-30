package com.ctsousa.mover.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public interface ImageIOService {

    BufferedImage read(InputStream inputStream) throws IOException;

    void write(BufferedImage imagem, String extension, ByteArrayOutputStream bytes) throws IOException;
}
