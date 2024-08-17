package com.ctsousa.mover.mapper;

import com.ctsousa.mover.core.entity.ModelEntity;
import com.ctsousa.mover.core.mapper.MapperToDomain;
import com.ctsousa.mover.core.mapper.MapperToResponse;
import com.ctsousa.mover.domain.Brand;
import com.ctsousa.mover.domain.Model;
import com.ctsousa.mover.request.ModelRequest;
import com.ctsousa.mover.response.ModelResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Essa classe esta depreciada, utilizar a classe de mapeamento com.ctsousa.mover.core.mapper.Transform,
 * Na classe VehicleResource tem exemplos de como utilizar.
 */
@Deprecated
@Component
public class ModelMapper implements MapperToDomain<Model, ModelRequest>, MapperToResponse<ModelResponse, ModelEntity> {

    @Override
    public Model toDomain(ModelRequest request) {
        Model domain  = new Model();
        domain.setId(request.getId());
        domain.setName(request.getName());
        domain.setActive(request.getActive());

        Brand brand = new Brand();
        brand.setId(request.getBrandId());
        domain.setBrand(brand);
        return domain;
    }

    @Override
    public ModelResponse toResponse(ModelEntity entity) {
        ModelResponse response = new ModelResponse();
        response.setId(entity.getId());
        response.setName(entity.getName().toUpperCase());
        response.setBrandId(entity.getBrand().getId());
        response.setBrandName(entity.getBrand().getName());
        response.setActive(entity.getActive());
        return response;
    }

    @Override
    public List<ModelResponse> toCollections(List<ModelEntity> entities) {
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }
}
