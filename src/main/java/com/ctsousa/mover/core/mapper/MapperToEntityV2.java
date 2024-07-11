package com.ctsousa.mover.core.mapper;

import com.ctsousa.mover.core.entity.AbstractEntity;

import java.util.List;

public interface MapperToEntityV2<E extends AbstractEntity> {

    E toEntity();
}
