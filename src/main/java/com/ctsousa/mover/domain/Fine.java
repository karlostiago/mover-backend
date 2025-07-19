package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.FineEntity;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Fine implements MapperToEntity<FineEntity> {

    @Override
    public FineEntity toEntity() {
        return null;
    }
}
