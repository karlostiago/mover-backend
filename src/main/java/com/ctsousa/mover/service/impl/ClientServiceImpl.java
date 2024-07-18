package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.notification.NotificationNotFoundException;
import com.ctsousa.mover.core.service.impl.AbstractServiceImpl;
import com.ctsousa.mover.core.validation.CpfValidator;
import com.ctsousa.mover.repository.ClientRepository;
import com.ctsousa.mover.service.ClientService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class ClientServiceImpl extends AbstractServiceImpl <ClientEntity, Long> implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(JpaRepository<ClientEntity, Long> repository, ClientRepository clientRepository) {
        super(repository);
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientEntity existsCpfRegistered(String cpf) {
        if (StringUtils.isBlank(cpf) || !CpfValidator.isValid(cpf)) {
            throw new NotificationException("CPF inválido ou não fornecido corretamente");
        }

        ClientEntity client = clientRepository.existsCpfRegisteredInApplication(cpf);
        if (client == null) {
            throw new NotificationNotFoundException("CPF não encontrado no sistema");
        }

        return client;
    }
}

