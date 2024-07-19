package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class Brand implements MapperToEntity<BrandEntity> {
    private Long id;
    private String name;
    private String symbol;
    private Boolean active;

    public void setName(String name) {
        if (StringUtils.isBlank(name)) throw new RuntimeException("");
        if (StringUtils.equalsIgnoreCase(name, "undefined"))throw new RuntimeException("");
        this.name = name.toUpperCase();
    }

    public void setSymbol(String symbol) {
        if (StringUtils.isBlank(symbol)) throw new RuntimeException("");
        if (StringUtils.equalsIgnoreCase(symbol, "undefined"))throw new RuntimeException("");
        this.symbol = symbol;
    }

    @Override
    public BrandEntity toEntity() {
        BrandEntity entity = new BrandEntity();
        entity.setId(this.getId());
        entity.setName(this.getName().toUpperCase());
        entity.setSymbol(this.getSymbol());
        entity.setActive(this.active);
        return entity;
    }
}
