package com.ctsousa.mover.core.util;

import com.ctsousa.mover.core.entity.InspectionPhotoEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.util.ByteArrayDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

public final class ImageUtil {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    public static String encodeImageToBase64(Path imagePath) throws IOException {
        byte[] imageBytes = Files.readAllBytes(imagePath);
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    public static byte[] decodeBase64ToImage(String base64Image) {
        return Base64.getDecoder().decode(base64Image);
    }

    public static boolean isPhotoValid(InspectionPhotoEntity photo) {
        return photo != null && photo.getPhotoEntity() != null && photo.getPhotoEntity().getImage() != null && !photo.getPhotoEntity().getImage().isEmpty();
    }

    public static void addPhotoAttachment(MimeMessageHelper helper, InspectionPhotoEntity photo) throws MessagingException {
        String base64Image = photo.getPhotoEntity().getImage();

        if (!base64Image.startsWith("data:image/")) {
            base64Image = convertFileToBase64(base64Image);
        }

        String mimeType = determineMimeType(base64Image);

        if (mimeType == null) {
            logger.warn("Formato de imagem não suportado para a foto com ID {}.", photo.getId());
            return;
        }

        base64Image = base64Image.replaceAll("^(data:image/(jpeg|jpg|png);base64,)?", "");
        byte[] photoBytes = ImageUtil.decodeBase64ToImage(base64Image);

        if (photoBytes == null || photoBytes.length == 0) {
            logger.warn("A foto com ID {} não pôde ser convertida para bytes.", photo.getId());
            return;
        }

        Long photoId = photo.getId();
        String attachmentName = photoId != null ? "foto_" + photoId : "foto_anonima";
        helper.addAttachment(attachmentName + "." + mimeType.split("/")[1], new ByteArrayDataSource(photoBytes, mimeType));
    }

    public static String convertFileToBase64(String filePath) {
        try {
            File file = new File(filePath);
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String base64String = Base64.getEncoder().encodeToString(fileContent);

            String mimeType = determineMimeTypeFromFileExtension(filePath);
            return "data:image/" + mimeType + ";base64," + base64String;
        } catch (IOException e) {
            logger.error("Erro ao ler o arquivo para conversão em Base64: {}", e.getMessage());
            return null;
        }
    }

    public static String determineMimeTypeFromFileExtension(String filePath) {
        if (filePath.endsWith(".jpeg") || filePath.endsWith(".jpg")) {
            return "jpeg";
        } else if (filePath.endsWith(".png")) {
            return "png";
        }
        logger.warn("Formato de imagem não suportado para o arquivo: {}", filePath);
        return "unknown";
    }

    public static String determineMimeType(String base64Image) {

        base64Image = base64Image.trim();

        if (base64Image.startsWith("data:image/jpeg;base64,")) {
            return "image/jpeg";
        } else if (base64Image.startsWith("data:image/jpg;base64,")) {
            return "image/jpeg";
        } else if (base64Image.startsWith("data:image/png;base64,")) {
            return "image/png";
        }

        logger.warn("Formato de imagem não suportado: {}", base64Image);
        return null;
    }

    public static String savePhoto(MultipartFile photo) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
        Path resourceDirectory = Paths.get("src", "main", "resources", "photos").toAbsolutePath().normalize();

        Files.createDirectories(resourceDirectory);
        Path filePath = resourceDirectory.resolve(fileName);

        Files.write(filePath, photo.getBytes());
        return filePath.toString();
    }
}
