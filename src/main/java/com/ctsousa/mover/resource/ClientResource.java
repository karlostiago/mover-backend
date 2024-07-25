package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.ClientApi;
import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.domain.Client;
import com.ctsousa.mover.domain.User;
import com.ctsousa.mover.mapper.ClientMapper;
import com.ctsousa.mover.mapper.UserMapper;
import com.ctsousa.mover.request.ClientRequest;
import com.ctsousa.mover.response.ClientResponse;
import com.ctsousa.mover.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clients")
public class ClientResource implements ClientApi {

    private final ClientService clientService;

    private final ClientMapper clientMapper;

    private final UserMapper userMapper;

    public ClientResource(ClientService clientService, ClientMapper clientMapper, UserMapper userMapper) {
        this.clientService = clientService;
        this.clientMapper = clientMapper;
        this.userMapper = userMapper;
    }

    @Override
    public ResponseEntity<ClientResponse> existingCpfRegister(String cpf) {
        ClientEntity entity = clientService.existsCpfRegistered(cpf);
        ClientResponse clientResponse = clientMapper.toResponse(entity);
        return ResponseEntity.ok(clientResponse);
    }

    @Override
    public ResponseEntity<ClientResponse> registerClientAndUser(ClientRequest clientRequest) {

        Client client = clientMapper.toDomain(clientRequest);
        User user = userMapper.toDomain(clientRequest.getUser());

        ClientEntity clientEntity = clientService.registerClientAndUser(client.toEntity(), user.toEntity());
        ClientResponse response = clientMapper.toResponse(clientEntity);

        return ResponseEntity.ok(response);
    }
}
