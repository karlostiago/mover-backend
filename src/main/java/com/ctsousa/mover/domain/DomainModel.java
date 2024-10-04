package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.AbstractEntity;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class DomainModel<T extends AbstractEntity> implements MapperToEntity<T> {
    protected Long id;
    protected Boolean active;

    public void setId(Long id) {
        this.id = id == 0 ? null : id;
    }
}
