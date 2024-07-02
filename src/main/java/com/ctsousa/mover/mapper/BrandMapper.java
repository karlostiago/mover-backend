package com.ctsousa.mover.mapper;

import com.ctsousa.mover.domain.Brand;
import com.ctsousa.mover.infrastructure.entity.BrandEntity;

import java.util.List;

public class BrandMapper implements MapperToEntity<BrandEntity, Brand> {

    @Override
    public BrandEntity toEntity(Brand domain) {
        return null;
    }

    @Override
    public List<BrandEntity> toCollections(List<Brand> domains) {
        return List.of();
    }
}
