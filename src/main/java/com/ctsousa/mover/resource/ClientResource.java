package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.ClientApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.domain.Client;
import com.ctsousa.mover.domain.Contact;
import com.ctsousa.mover.domain.User;
import com.ctsousa.mover.enumeration.BrazilianStates;
import com.ctsousa.mover.enumeration.TypePerson;
import com.ctsousa.mover.request.ClientRequest;
import com.ctsousa.mover.request.ContactRequest;
import com.ctsousa.mover.response.BrazilianStatesResponse;
import com.ctsousa.mover.response.ClientResponse;
import com.ctsousa.mover.response.TypePersonResponse;
import com.ctsousa.mover.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;
import static com.ctsousa.mover.core.validation.PasswordValidator.defaultPasswordMover;

@RestController
@RequestMapping("/clients")
public class ClientResource extends BaseResource<ClientResponse, ClientRequest, ClientEntity> implements ClientApi {

    @Autowired
    private ClientService clientService;

    public ClientResource(ClientService clientService) {
        super(clientService);
    }

    @Override
    public ResponseEntity<ClientResponse> add(ClientRequest request) {
        Client domain = toMapper(request, Client.class);
        ClientEntity entity = clientService.save(domain.toEntity());

        return ResponseEntity.ok(toMapper(entity, ClientResponse.class));
    }

    @Override
    public ResponseEntity<ClientResponse> update(Long id, ClientRequest request) {
        clientService.existsById(id);
        Client clientDomain = toMapper(request, Client.class);
        ClientEntity entity = clientDomain.toEntity();
        clientService.save(entity);
        return ResponseEntity.ok(toMapper(entity, ClientResponse.class));
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
    public ResponseEntity<ClientResponse> findAddress(Integer postalCode) {
        ClientEntity entity = clientService.findByAddress(postalCode);
        if (entity == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(toMapper(entity, ClientResponse.class));
    }

    @Override
    public ResponseEntity<List<ClientResponse>> filterBy(String search) {
        List<ClientEntity> entities = clientService.filterBy(search);
        return ResponseEntity.ok(toCollection(entities, ClientResponse.class));
    }

    @Override
    public ResponseEntity<ClientResponse> existingCpfRegister(String cpf) {
        ClientEntity entity = clientService.existsCpfRegistered(cpf);
        return ResponseEntity.ok(toMapper(entity, ClientResponse.class));
    }

    @Override
    public ResponseEntity<ClientResponse> registerClientAndUser(ClientRequest request) {
        Client domain = toMapper(request, Client.class);
        String password = domain.getUser() != null ? domain.getUser().getPassword() : defaultPasswordMover();
        ClientEntity entity = clientService.registerClient(domain.toEntity(), password);
        return ResponseEntity.ok(toMapper(entity, ClientResponse.class));
    }

    @Override
    public ResponseEntity<List<ClientResponse>> onlyAvailable() {
        List<ClientEntity> entities = clientService.onlyAvailable();
        return ResponseEntity.ok(toCollection(entities, ClientResponse.class));
    }

    @Override
    public Class<?> responseClass() {
        return ClientResponse.class;
    }
}
