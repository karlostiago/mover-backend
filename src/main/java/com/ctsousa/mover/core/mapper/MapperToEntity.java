package com.ctsousa.mover.core.mapper;

import com.ctsousa.mover.infrastructure.entity.AbstractEntity;

import java.util.List;

public interface MapperToEntity<E extends AbstractEntity, D> {

    E toEntity(D domain);

    List<E> toEntities(List<D> domains);
}
