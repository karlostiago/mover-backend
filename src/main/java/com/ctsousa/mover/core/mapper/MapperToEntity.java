package com.ctsousa.mover.core.mapper;

import com.ctsousa.mover.core.entity.AbstractEntity;

public interface MapperToEntity<E extends AbstractEntity> {

    E toEntity();
}
