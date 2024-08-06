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
    private String color;
    private Integer yearManufacture;
    private Integer yearModel;
    private Boolean active;
    private Brand brand;

    public void setName(String name) {
        if (StringUtils.isBlank(name)) throw new NotificationException("Informe uma descrição.");
        if (StringUtils.equalsIgnoreCase(name, "undefined")) throw new NotificationException("Informe uma descrição.");
        this.name = name.toUpperCase();
    }

    public void setColor(String color) {
        if (StringUtils.isBlank(color)) throw new NotificationException("Informe uma cor.");
        if (StringUtils.equalsIgnoreCase(color, "undefined")) throw new NotificationException("Informe uma cor.");
        this.color = color.toUpperCase();
    }

    public void setBrand(Brand brand) {
        if (brand == null || brand.getId() == null) throw new NotificationException("Selecione uma marca.");
        this.brand = brand;
    }

    public void setYearManufacture(Integer yearManufacture) {
        if (yearManufacture == null || yearManufacture < 0) throw new NotificationException("Ano fabricação inválida.");
        if (yearManufacture.toString().length() != 4) throw new NotificationException("Ano fabricação inválida.");
        this.yearManufacture = yearManufacture;
    }

    public void setYearModel(Integer yearModel) {
        if (yearModel == null || yearModel < 0) throw new NotificationException("Ano do modelo inválida.");
        if (yearModel.toString().length() != 4) throw new NotificationException("Ano do modelo inválida.");
        this.yearModel = yearModel;
    }

    @Override
    public ModelEntity toEntity() {
        ModelEntity entity = new ModelEntity();
        entity.setId(this.getId());
        entity.setName(this.getName());
        entity.setColor(this.getColor());
        entity.setYearModel(this.getYearModel());
        entity.setYearManufacture(this.getYearManufacture());
        entity.setActive(this.getActive());

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(this.getBrand().getId());
        brandEntity.setName(this.getBrand().getName());
        entity.setBrand(brandEntity);
        return entity;
    }
}
