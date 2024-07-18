package com.ctsousa.mover.mapper;

import com.ctsousa.mover.core.entity.SenderEntity;
import com.ctsousa.mover.core.mapper.MapperToResponse;
import com.ctsousa.mover.response.SenderResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SenderMapper implements MapperToResponse<SenderResponse, SenderEntity> {
    @Override
    public SenderResponse toResponse(SenderEntity entity) {
        SenderResponse response = new SenderResponse();
        response.setCode(entity.getCode());
        response.setEmail(entity.getEmail());
        response.setExpiryDate(entity.getExpiryDate());
        response.setClientId(entity.getClientId());
        response.setId(entity.getId());
        return response;
    }

    @Override
    public List<SenderResponse> toCollections(List<SenderEntity> entities) {
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }
}
