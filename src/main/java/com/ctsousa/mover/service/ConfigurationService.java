package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.ConfigurationEntity;
import com.ctsousa.mover.core.service.BaseService;

import java.util.List;

public interface ConfigurationService extends BaseService<ConfigurationEntity, Long> {

    List<ConfigurationEntity> filterBy(String search);

    @Override
    ConfigurationEntity findById(Long aLong);

    boolean verifyKeySystem(final String key);
}
