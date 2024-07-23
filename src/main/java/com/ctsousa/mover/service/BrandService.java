package com.ctsousa.mover.service;

import com.ctsousa.mover.core.service.AbstractService;
import com.ctsousa.mover.core.entity.BrandEntity;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

public interface BrandService extends AbstractService<BrandEntity, Long> {

    BufferedImage upload(MultipartFile file);

    String toBase64(BufferedImage image);
}
