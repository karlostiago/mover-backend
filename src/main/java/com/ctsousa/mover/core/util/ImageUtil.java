package com.ctsousa.mover.core.util;

import com.ctsousa.mover.core.entity.InspectionPhotoEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.MimeMessageHelper;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;

public final class ImageUtil {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    public static void addPhotoAttachment(MimeMessageHelper helper, InspectionPhotoEntity photo) throws MessagingException {

        String base64Image = photo.getPhotoEntity().getImage();

        if (base64Image == null || base64Image.isEmpty() || !base64Image.startsWith("data:image/")) {

            base64Image = convertFileToBase64(photo.getPhotoEntity().getImage());
        }

        String mimeType = determineMimeType(base64Image);
        if (mimeType == null) {
            logger.warn("Formato de imagem não suportado para a foto com ID {}.", photo.getId());
            return;
        }

        String imageData = base64Image.split(",")[1];
        byte[] imageBytes = Base64.getDecoder().decode(imageData);

        String filename = "photo_" + photo.getId() + "." + mimeType.split("/")[1];
        helper.addAttachment(filename, new InputStreamSource() {
            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(imageBytes);
            }
        }, mimeType);
    }

    public static String convertFileToBase64(String filePath) {
        try {
            File file = new File(filePath);

            if (!file.exists() || !file.isFile()) {
                logger.error("Arquivo não encontrado ou não é um arquivo válido: {}", filePath);
                throw new NotificationException("Arquivo não encontrado: " + filePath, Severity.ERROR);
            }

            byte[] fileContent = Files.readAllBytes(file.toPath());
            String base64String = Base64.getEncoder().encodeToString(fileContent);

            String mimeType = determineMimeTypeFromFileExtension(filePath);
            return "data:image/" + mimeType + ";base64," + base64String;
        } catch (IOException e) {
            logger.error("Erro ao ler o arquivo para conversão em Base64: {}", e.getMessage());
            throw new NotificationException("Erro ao converter arquivo para Base64: " + e.getMessage(), Severity.ERROR);
        }
    }

    public static String determineMimeTypeFromFileExtension(String filePath) {
        if (filePath.endsWith(".jpeg") || filePath.endsWith(".jpg")) {
            return "jpeg";
        } else if (filePath.endsWith(".png")) {
            return "png";
        } else if (filePath.endsWith(".gif")) {
            return "gif";
        } else if (filePath.endsWith(".webp")) {
            return "webp";
        }
        logger.warn("Formato de imagem não suportado para o arquivo: {}", filePath);
        return "unknown";
    }

    public static String determineMimeType(String base64Image) {
        if (base64Image == null || base64Image.isEmpty()) {
            logger.warn("A imagem base64 está vazia ou nula.");
            return null;
        }

        base64Image = base64Image.trim();

        if (base64Image.startsWith("data:image/jpeg;base64,")) {
            return "image/jpeg";
        } else if (base64Image.startsWith("data:image/jpg;base64,")) {
            return "image/jpeg";
        } else if (base64Image.startsWith("data:image/png;base64,")) {
            return "image/png";
        } else if (base64Image.startsWith("data:image/gif;base64,")) {
            return "image/gif";
        } else if (base64Image.startsWith("data:image/webp;base64,")) {
            return "image/webp";
        }

        logger.warn("Formato de imagem não suportado: {}", base64Image);
        return null;
    }
}
