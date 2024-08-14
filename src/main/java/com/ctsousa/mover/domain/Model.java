package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.entity.ModelEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class Model implements MapperToEntity<ModelEntity> {

    private Long id;
    private String name;
    private Boolean active;
    private Brand brand;

    public void setName(String name) {
        if (StringUtils.isBlank(name)) throw new NotificationException("Informe uma descrição.");
        if (StringUtils.equalsIgnoreCase(name, "undefined")) throw new NotificationException("Informe uma descrição.");
        this.name = name.toUpperCase();
    }

    public void setBrand(Brand brand) {
        if (brand == null || brand.getId() == null) throw new NotificationException("Selecione uma marca.");
        this.brand = brand;
    }

    @Override
    public ModelEntity toEntity() {
        ModelEntity entity = new ModelEntity();
        entity.setId(this.getId());
        entity.setName(this.getName().toUpperCase());
        entity.setActive(this.getActive());

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(this.getBrand().getId());
        entity.setBrand(brandEntity);
        return entity;
    }
}
