package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.service.AbstractService;

public interface ClientService extends AbstractService<ClientEntity, Long> {
    ClientEntity existsCpfRegistered(String cpf);
}
