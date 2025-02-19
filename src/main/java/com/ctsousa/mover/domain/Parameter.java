package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.ParameterEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.enumeration.TypeValueParameter;
import lombok.Getter;
import lombok.Setter;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Getter
@Setter
public class Parameter extends DomainModel<ParameterEntity> {

    private String key;
    private String value;
    private String typeValue;
    private Boolean active;

    @Override
    public ParameterEntity toEntity() {
        TypeValueParameter type = TypeValueParameter.toDescription(this.typeValue);

        ParameterEntity entity = new ParameterEntity();
        entity.setId(this.getId());
        entity.setKey(toUppercase(this.getKey()));
        entity.setValue(TypeValueParameter.TEXT.equals(type) ? toUppercase(this.getValue()) : this.getValue());
        entity.setTypeValue(type.name());
        entity.setActive(this.getActive());

        if (value == null || value.trim().isEmpty() || value.equalsIgnoreCase("0")) {
            throw new NotificationException("Valor inválido.");
        }

        return entity;
    }
}
