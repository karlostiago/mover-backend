package com.ctsousa.mover.service;

import com.ctsousa.mover.core.service.BaseService;
import com.ctsousa.mover.core.entity.BrandEntity;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.List;

public interface BrandService extends BaseService<BrandEntity, Long> {

    BufferedImage upload(MultipartFile file);

    String toBase64(BufferedImage image);

    List<BrandEntity> filterByName(String name);
}
