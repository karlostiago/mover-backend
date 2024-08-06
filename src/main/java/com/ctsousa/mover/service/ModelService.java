package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.ModelEntity;
import com.ctsousa.mover.core.service.AbstractService;

import java.util.List;

public interface ModelService extends AbstractService<ModelEntity, Long> {

    List<ModelEntity> findBy(String paramFilter);
}
