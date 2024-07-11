package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.service.AbstractService;
import com.ctsousa.mover.service.customServices.CustomClientService;

public interface ClientService extends AbstractService<ClientEntity, Long>, CustomClientService {
    ClientEntity existsCpfRegistered(String cpf);
}
