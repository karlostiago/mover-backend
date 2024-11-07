package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.InspectionEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Inspection extends DomainModel<InspectionEntity> {

    @Override
    public InspectionEntity toEntity() {
        return null;
    }
}
