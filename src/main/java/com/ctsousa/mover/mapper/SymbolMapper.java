package com.ctsousa.mover.mapper;

import com.ctsousa.mover.core.entity.SymbolEntity;
import com.ctsousa.mover.core.mapper.MapperToResponse;
import com.ctsousa.mover.response.SymbolResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SymbolMapper implements MapperToResponse<SymbolResponse, SymbolEntity> {

    @Override
    public SymbolResponse toResponse(SymbolEntity entity) {
        SymbolResponse response = new SymbolResponse();
        response.setId(entity.getId());
        response.setDescription(entity.getDescription().toUpperCase());
        response.setImageBase64(entity.getImageBase64());
        return response;
    }

    @Override
    public List<SymbolResponse> toCollections(List<SymbolEntity> entities) {
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }
}
