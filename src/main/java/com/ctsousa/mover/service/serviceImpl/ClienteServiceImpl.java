package com.ctsousa.mover.service.serviceImpl;

import com.ctsousa.mover.entity.ClientEntity;
import com.ctsousa.mover.repository.ClientRepository;
import com.ctsousa.mover.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClienteServiceImpl implements IClienteService {

    private final  ClientRepository clientRepository;

    @Autowired
    public ClienteServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientEntity findClientByCpf(String cpf) {
        return clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Cliente com CPF " + cpf + " não encontrado."));
    }

}
