package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.ModelEntity;
import com.ctsousa.mover.core.service.BaseService;

import java.util.List;

public interface ModelService extends BaseService<ModelEntity, Long> {

    List<ModelEntity> findBy(String paramFilter);

    List<ModelEntity> findByBrandId(Long brandId);
}
