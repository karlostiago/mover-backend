package com.ctsousa.mover.core.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public final class ImageUtil {
    public static String encodeImageToBase64(Path imagePath) throws IOException {
        byte[] imageBytes = Files.readAllBytes(imagePath);
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    public static byte[] decodeBase64ToImage(String base64Image) {
        return Base64.getDecoder().decode(base64Image);
    }
}
