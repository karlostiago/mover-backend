package com.ctsousa.mover.mapper;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.mapper.MapperToDomain;
import com.ctsousa.mover.core.mapper.MapperToResponse;
import com.ctsousa.mover.domain.Brand;
import com.ctsousa.mover.request.BrandRequest;
import com.ctsousa.mover.response.BrandResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BrandMapper implements MapperToDomain<Brand, BrandRequest>, MapperToResponse<BrandResponse, BrandEntity> {

    @Override
    public Brand toDomain(BrandRequest request) {
        Brand domain  = new Brand();
        domain.setId(request.getId());
        domain.setName(request.getName());
        domain.setSymbol(request.getSymbol());
        domain.setActive(request.getActive());
        return domain;
    }

    @Override
    public BrandResponse toResponse(BrandEntity entity) {
        BrandResponse response = new BrandResponse();
        response.setId(entity.getId());
        response.setName(entity.getName().toUpperCase());
        response.setSymbol(entity.getSymbol());
        response.setActive(entity.getActive());
        return response;
    }

    @Override
    public List<BrandResponse> toCollections(List<BrandEntity> entities) {
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }
}
