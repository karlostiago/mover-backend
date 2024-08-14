package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.service.BaseService;

public interface ClientService extends BaseService<ClientEntity, Long> {
    ClientEntity existsCpfRegistered(String cpf);
    ClientEntity registerClient(ClientEntity client, String password);
    ClientEntity updateClient(Long clientId, ClientEntity clientUpdates);

}
