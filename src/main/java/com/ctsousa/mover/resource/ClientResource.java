package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.ClientApi;
import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.domain.Client;
import com.ctsousa.mover.mapper.ClientMapper;
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

    public ClientResource(ClientService clientService, ClientMapper clientMapper) {
        this.clientService = clientService;
        this.clientMapper = clientMapper;
    }

    @Override
    public ResponseEntity<ClientResponse> existingCpfRegister(String cpf) {
        ClientEntity entity = clientService.existsCpfRegistered(cpf);
        ClientResponse clientResponse = clientMapper.toResponse(entity);
        return ResponseEntity.ok(clientResponse);
    }
}
