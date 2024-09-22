package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.ConfigurationEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.util.StringUtil;
import com.ctsousa.mover.enumeration.TypeValueConfiguration;
import lombok.Getter;
import lombok.Setter;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Getter
@Setter
public class Configuration extends DomainModel<ConfigurationEntity> {

    private String key;
    private String value;
    private String typeValue;
    private Boolean active;

    @Override
    public ConfigurationEntity toEntity() {
        TypeValueConfiguration type = TypeValueConfiguration.toDescription(this.typeValue);

        ConfigurationEntity entity = new ConfigurationEntity();
        entity.setId(this.getId());
        entity.setKey(toUppercase(this.getKey()));
        entity.setValue(TypeValueConfiguration.TEXT.equals(type) ? toUppercase(this.getValue()) : this.getValue());
        entity.setTypeValue(type.name());
        entity.setActive(this.getActive());

        if (value == null || value.trim().isEmpty() || value.equalsIgnoreCase("0")) {
            throw new NotificationException("Valor inválido.");
        }

        return entity;
    }
}
