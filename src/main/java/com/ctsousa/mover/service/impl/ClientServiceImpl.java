package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.service.impl.AbstractServiceImpl;
import com.ctsousa.mover.core.validation.CpfValidator;
import com.ctsousa.mover.repository.ClientRepository;
import com.ctsousa.mover.service.ClientService;
import com.ctsousa.mover.service.customServices.CustomClientService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class ClientServiceImpl extends AbstractServiceImpl <ClientEntity, Long> implements ClientService, CustomClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(JpaRepository<ClientEntity, Long> repository, ClientRepository clientRepository) {
        super(repository);
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientEntity existsCpfRegistered(String cpf) {

        if (StringUtils.isBlank(cpf)) {
            throw new NotificationException("CPF não fornecido corretamente");
        }

        if (!CpfValidator.isValid(cpf)) {
            throw new NotificationException("CPF inválido");
        }

        ClientEntity client = clientRepository.existsCpfRegisteredInApplication(cpf);
        if (client == null) {
            throw new NotificationException("CPF não encontrado no sistema");
        }

        return client;
    }
}

