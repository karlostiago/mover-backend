package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.ParameterEntity;
import com.ctsousa.mover.core.service.BaseService;

import java.util.List;

public interface ParameterService extends BaseService<ParameterEntity, Long> {

    List<ParameterEntity> filterBy(String search);

    @Override
    ParameterEntity findById(Long aLong);

    boolean verifyKeySystem(final String key);
}
