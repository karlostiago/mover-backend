package com.ctsousa.mover.service.customServices;

import com.ctsousa.mover.core.entity.ClientEntity;

public interface CustomClientService {
    ClientEntity existsCpfRegistered(String cpf);
}
