package com.ctsousa.mover.mapper;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.mapper.MapperToResponse;
import com.ctsousa.mover.response.ClientResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientMapper implements MapperToResponse<ClientResponse, ClientEntity> {
    @Override
    public ClientResponse toResponse(ClientEntity entity) {
        ClientResponse response = new ClientResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setCpf(entity.getCpf());
        response.setEmail(entity.getEmail());
        response.setNumber(entity.getNumber());
        return response;
    }

    @Override
    public List<ClientResponse> toCollections(List<ClientEntity> entities) {
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }
}
