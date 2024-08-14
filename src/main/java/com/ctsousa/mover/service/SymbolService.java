package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.SymbolEntity;
import com.ctsousa.mover.core.service.BaseService;

public interface SymbolService extends BaseService<SymbolEntity, Long> {

    void deleteById(Long id);
}
