package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.AbstractEntity;
import com.ctsousa.mover.core.mapper.MapperToEntity;

public abstract class DomainModel<T extends AbstractEntity> implements MapperToEntity<T> {
}
