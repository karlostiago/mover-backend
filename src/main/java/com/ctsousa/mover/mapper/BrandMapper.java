package com.ctsousa.mover.mapper;

import com.ctsousa.mover.core.mapper.MapperToDomain;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import com.ctsousa.mover.domain.Brand;
import com.ctsousa.mover.core.entity.BrandEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BrandMapper implements MapperToEntity<BrandEntity, Brand>, MapperToDomain<Brand, BrandEntity> {

    @Override
    public BrandEntity toEntity(Brand domain) {
        BrandEntity entity = new BrandEntity();
        entity.setName(domain.getName());
        entity.setSymbol(domain.getSymbol());
        entity.setAsset(Boolean.TRUE);
        return entity;
    }

    @Override
    public List<BrandEntity> toEntities(List<Brand> domains) {
        return domains.stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public Brand toDomain(BrandEntity entity) {
        Brand brand = new Brand();
        brand.setId(entity.getId());
        brand.setName(entity.getName());
        brand.setSymbol(entity.getSymbol());
        return brand;
    }

    @Override
    public List<Brand> toDomains(List<BrandEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }
}
