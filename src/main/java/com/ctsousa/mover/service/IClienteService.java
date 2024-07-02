package com.ctsousa.mover.service;

import com.ctsousa.mover.entity.ClientEntity;

public interface IClienteService {
    ClientEntity findClientByCpf(String cpf);
}
