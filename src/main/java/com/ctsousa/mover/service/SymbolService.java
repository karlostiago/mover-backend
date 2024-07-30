package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.SymbolEntity;
import com.ctsousa.mover.core.service.AbstractService;

public interface SymbolService extends AbstractService<SymbolEntity, Long> {

    void deleteById(Long id);
}
