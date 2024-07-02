package com.ctsousa.mover.core.mapper;

import com.ctsousa.mover.infrastructure.entity.AbstractEntity;

import java.util.List;

public interface MapperToDomain<D, E extends AbstractEntity> {

    D toDomain(E entity);

    List<D> toDomains(List<E> entities);
}
