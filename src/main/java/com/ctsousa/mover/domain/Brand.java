package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class Brand implements MapperToEntity<BrandEntity> {
    private Long id;
    private String name;
    private Symbol symbol;
    private Boolean active;

    public void setName(String name) {
        if (StringUtils.isBlank(name)) throw new NotificationException("Informe uma descrição.");
        if (StringUtils.equalsIgnoreCase(name, "undefined")) throw new NotificationException("Informe uma descrição.");
        this.name = name.toUpperCase();
    }

    public void setSymbol(Symbol symbol) {
        if (symbol.getId() == 0) throw new NotificationException("Selecione um simbolo");
        this.symbol = symbol;
    }

    @Override
    public BrandEntity toEntity() {
        BrandEntity entity = new BrandEntity();
        entity.setId(this.getId());
        entity.setName(this.getName().toUpperCase());
        entity.setSymbol(this.getSymbol().toEntity());
        entity.setActive(this.active);
        return entity;
    }
}
