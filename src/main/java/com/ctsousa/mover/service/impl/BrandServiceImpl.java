package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.AbstractServiceImpl;
import com.ctsousa.mover.repository.BrandRepository;
import com.ctsousa.mover.service.BrandService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.GZIPOutputStream;

@Component
public class BrandServiceImpl extends AbstractServiceImpl<BrandEntity, Long> implements BrandService {

    private final BrandRepository brandRepository;

    public BrandServiceImpl(JpaRepository<BrandEntity, Long> repository) {
        super(repository);
        this.brandRepository = (BrandRepository) repository;
    }

    @Override
    public List<BrandEntity> filterByName(String name) {
        return brandRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public BrandEntity save(BrandEntity entity) {
        if (entity.getId() == null) {
            if (brandRepository.existsByName(entity.getName())) throw new NotificationException("Existe uma marca, cadastrada com o nome informado.", Severity.INFO);
        }
        return super.save(entity);
    }

    @Override
    public BufferedImage upload(MultipartFile file) {
        String filename = file.getOriginalFilename();

        if (filename == null || !filename.endsWith(".png")) {
            throw new NotificationException("O arquivo deve ter a extensão .png");
        }

        try(InputStream inputStream = file.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);

            if (image == null) {
                throw new NotificationException("Arquivo não é uma imagem válida.");
            }

            int width = image.getWidth();
            int height = image.getHeight();

//            if (width != height) {
//                throw new NotificationException("A larguta e altura da imagem são diferentes");
//            }

            return image;

        } catch (IOException e) {
            throw new NotificationException(e.getMessage());
        }
    }

    public String toBase64(BufferedImage image) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            byte [] imageBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new NotificationException(e.getMessage());
        }
    }
}
