package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.ClientV2Api;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.domain.Client;
import com.ctsousa.mover.enumeration.BrazilianStates;
import com.ctsousa.mover.enumeration.TypePerson;
import com.ctsousa.mover.request.ClientV2Request;
import com.ctsousa.mover.response.BrazilianStatesResponse;
import com.ctsousa.mover.response.ClientV2Response;
import com.ctsousa.mover.response.TypePersonResponse;
import com.ctsousa.mover.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/v2/clients")
public class ClientV2Resource extends BaseResource<ClientV2Response, ClientV2Request, ClientEntity> implements ClientV2Api {

    @Autowired
    private ClientService clientService;

    public ClientV2Resource(ClientService clientService) {
        super(clientService);
    }

    @Override
    public ResponseEntity<ClientV2Response> add(ClientV2Request request) {
        Client domain = toMapper(request, Client.class);
        ClientEntity entity = clientService.save(domain.toEntity());
        return ResponseEntity.ok(toMapper(entity, ClientV2Response.class));
    }

    @Override
    public ResponseEntity<ClientV2Response> update(Long id, ClientV2Request request) {
        return null;
    }

    @Override
    public ResponseEntity<List<BrazilianStatesResponse>> findAllBrazilianStates() {
        List<BrazilianStates> states = List.of(BrazilianStates.values());
        return ResponseEntity.ok(toCollection(states, BrazilianStatesResponse.class));
    }

    @Override
    public ResponseEntity<List<TypePersonResponse>> findAllTypePerson() {
        List<TypePerson> types = List.of(TypePerson.values());
        return ResponseEntity.ok(toCollection(types, TypePersonResponse.class));
    }

    @Override
    public ResponseEntity<ClientV2Response> findAddress(Integer postalCode) {
        ClientEntity entity = clientService.findByAddress(postalCode);
        if (entity == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(toMapper(entity, ClientV2Response.class));
    }

    @Override
    public Class<?> responseClass() {
        return ClientV2Response.class;
    }
}
