package com.ctsousa.mover.core.mapper;

import com.ctsousa.mover.core.entity.AbstractEntity;

import java.util.List;

public interface MapperToResponse<R, D extends AbstractEntity> {

    R toResponse(D entity);

    List<R> toCollections(List<D> list);
}
