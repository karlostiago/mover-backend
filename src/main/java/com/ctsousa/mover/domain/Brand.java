package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Brand implements MapperToEntity<BrandEntity> {
    private Long id;
    private String name;
    private String symbol;
    private Boolean active;

    public void setName(String name) {
        if (name == null) throw new RuntimeException("");
        if (name.isEmpty()) throw new RuntimeException("");
        if (name.equalsIgnoreCase("undefined")) throw new RuntimeException("");
        this.name = name.toUpperCase();
    }

    public void setSymbol(String symbol) {
        if (symbol == null) throw new RuntimeException("");
        if (symbol.isEmpty()) throw new RuntimeException("");
        if (symbol.equalsIgnoreCase("undefined")) throw new RuntimeException("");
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
