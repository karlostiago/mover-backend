package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.ModelEntity;
import com.ctsousa.mover.core.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ModelService extends BaseService<ModelEntity, Long> {

    Page<ModelEntity> findBy(String paramFilter, Pageable pageable);

    List<ModelEntity> findByBrandId(Long brandId);

    Page<ModelEntity> findAll(Pageable pageable);
}
