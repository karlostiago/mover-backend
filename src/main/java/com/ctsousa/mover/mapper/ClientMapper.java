package com.ctsousa.mover.mapper;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.mapper.MapperToDomain;
import com.ctsousa.mover.core.mapper.MapperToResponse;
import com.ctsousa.mover.domain.Client;
import com.ctsousa.mover.domain.User;
import com.ctsousa.mover.request.ClientRequest;
import com.ctsousa.mover.response.ClientResponse;
import com.ctsousa.mover.response.UserResponse;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Essa classe esta depreciada, utilizar a classe de mapeamento com.ctsousa.mover.core.mapper.Transform,
 * Na classe VehicleResource tem exemplos de como utilizar.
 */
@Deprecated
@Component
public class ClientMapper implements MapperToDomain<Client, ClientRequest>, MapperToResponse<ClientResponse, ClientEntity> {

    private final UserMapper userMapper;

    public ClientMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public ClientResponse toResponse(ClientEntity entity) {
        ClientResponse response = new ClientResponse();
        response.setId(entity.getId());
        response.setRg(entity.getRg());
//        response.setCep(entity.getCep());
        response.setState(entity.getState());
        response.setName(entity.getName());
        response.setCpf(entity.getCpfCnpj());
        response.setEmail(entity.getEmail());
        response.setNumber(entity.getNumber());
        if (entity.getUser() != null) {
            UserResponse userResponse = userMapper.toResponse(entity.getUser());
            response.setUser(userResponse);
        }
        return response;
    }

    @Override
    public List<ClientResponse> toCollections(List<ClientEntity> entities) {
        return entities.stream().map(this::toResponse).toList();
    }

    @Override
    public Client toDomain(ClientRequest request) {
        Client client = new Client();
        client.setId(request.getId());
        client.setName(request.getName());
//        client.setCpf(request.getCpf());
        client.setEmail(request.getEmail());
        client.setNumber(request.getNumber());
        client.setRg(request.getRg());
        client.setBirthDate(request.getBirthDate());
//        client.setCep(request.getCep());
//        client.setState(request.getState());

        if (request.getUser() != null) {
            User user = userMapper.toDomain(request.getUser());
            client.setUser(user);
        }
        return client;
    }
}
