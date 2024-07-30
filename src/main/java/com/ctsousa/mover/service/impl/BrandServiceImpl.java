package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.AbstractServiceImpl;
import com.ctsousa.mover.repository.BrandRepository;
import com.ctsousa.mover.service.BrandService;
import com.ctsousa.mover.service.ImageIOService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Component
public class BrandServiceImpl extends AbstractServiceImpl<BrandEntity, Long> implements BrandService {

    private final BrandRepository brandRepository;

    private final ImageIOService imageIOService;

    public BrandServiceImpl(JpaRepository<BrandEntity, Long> repository, ImageIOService imageIOService) {
        super(repository);
        this.brandRepository = (BrandRepository) repository;
        this.imageIOService = imageIOService;
    }

    @Override
    public List<BrandEntity> filterByName(String name) {
        return brandRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public BrandEntity save(BrandEntity entity) {
        if (entity.isNew()) {
            if (brandRepository.existsByName(entity.getName())) {
                throw new NotificationException("Existe uma marca, cadastrada com o nome informado.", Severity.WARNING);
            }
        } else if (!entity.isNew()) {
            if (brandRepository.existsByNameNotId(entity.getName(), entity.getId())) {
                throw new NotificationException("Não foi possível atualizar, pois já tem uma marca, com o nome informado.", Severity.WARNING);
            }
        }
        return super.save(entity);
    }

    @Override
    public BufferedImage upload(MultipartFile file) {
        String filename = Objects.requireNonNull(file.getOriginalFilename()).isEmpty() ? null : file.getOriginalFilename();

        if (filename == null) {
            throw new NotificationException("Nenhum arquivo carregado.");
        }

        if (!filename.endsWith(".png")) {
            throw new NotificationException("O arquivo deve ter a extensão .png");
        }

        try(InputStream inputStream = file.getInputStream()) {
            BufferedImage image = imageIOService.read(inputStream);

            if (image == null) {
                throw new NotificationException("Arquivo não é uma imagem válida.");
            }

            return image;

        } catch (IOException e) {
            throw new NotificationException(e.getMessage());
        }
    }

    public String toBase64(BufferedImage image) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            imageIOService.write(image, "png", outputStream);
            byte [] imageBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new NotificationException(e.getMessage());
        }
    }
}
