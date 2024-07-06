package com.ctsousa.mover.mapper;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.mapper.MapperToDomain;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import com.ctsousa.mover.domain.Client;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ClientMapper implements MapperToEntity<ClientEntity, Client>, MapperToDomain<Client, ClientEntity> {

    @Override
    public Client toDomain(ClientEntity entity) {
        Client client = new Client();
        client.setId(entity.getId());
        client.setName(entity.getName());
        client.setCpf(entity.getCpf());
        client.setEmail(entity.getEmail());
        return client;
    }

    @Override
    public List<Client> toDomains(List<ClientEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public ClientEntity toEntity(Client domain) {
        ClientEntity client = new ClientEntity();
        client.setId(domain.getId());
        client.setCpf(domain.getCpf());
        client.setEmail(domain.getEmail());
        client.setName(domain.getName());
        return client;
    }

    @Override
    public List<ClientEntity> toEntities(List<Client> domains) {
        return domains.stream()
                .map(this::toEntity)
                .toList();
    }
}
