package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.service.BaseService;

import java.util.List;

public interface ClientService extends BaseService<ClientEntity, Long> {
    ClientEntity existsCpfRegistered(String cpf);
    ClientEntity registerClient(ClientEntity client, String password);
    ClientEntity updateClient(Long clientId, ClientEntity clientUpdates);

    ClientEntity findByAddress(Integer postalCode);

    List<ClientEntity> filterBy(String search);
}
