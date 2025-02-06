package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.entity.ContractEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.enumeration.Situation;
import com.ctsousa.mover.repository.ClientRepository;
import com.ctsousa.mover.repository.ContractRepository;
import com.ctsousa.mover.service.ClientService;
import com.ctsousa.mover.service.ContractService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class ContractServiceImpl extends BaseServiceImpl<ContractEntity, Long> implements ContractService {

    private final ContractRepository repository;

    private final ClientRepository clientRepository;

    private final ClientService clientService;

    private final ContractRepository contractRepository;

    public ContractServiceImpl(ContractRepository repository,
                               ContractRepository repository1,
                               ClientRepository clientRepository,
                               ClientService clientService,
                               ContractRepository contractRepository) {

        super(repository);
        this.repository = repository1;
        this.clientRepository = clientRepository;
        this.clientService = clientService;
        this.contractRepository = contractRepository;
    }

    @Override
    @Transactional
    public ContractEntity save(ContractEntity entity) {
        validContract(entity);
        if (entity.isNew()) {
            if (repository.existsByNumber(entity.getNumber())) {
                throw new NotificationException("Ocorreu um erro ao salvar o contrato. Numeração em duplicidade!", Severity.ERROR);
            }
        } else {
            if (Situation.CLOSED.equals(entity.getSituation())) {
                return close(entity);
            }
        }
        return super.save(entity);
    }

    @Override
    public ContractEntity close(ContractEntity entity) {
        entity.setClient(clientService.findById(entity.getClient().getId()));
        ClientEntity clientEntity = entity.getClient();
        clientEntity.setActive(Boolean.FALSE);
        clientRepository.save(clientEntity);

        entity.setSituation(Situation.CLOSED);
        entity.setActive(Boolean.FALSE);
        entity.setEndDate(LocalDate.now());
        repository.save(entity);
        
        return entity;
    }

    @Override
    public Optional<ContractEntity> findContratoByClientId(Long id) {
        return contractRepository.findContratoByClientId(id);
    }

    private void validContract(ContractEntity entity) {
        avaliableToClient(entity.getClient());
        canTerminate(entity);
        billingStartDate(entity);
        depositAmount(entity);
        recurrenceValue(entity);
    }

    private void billingStartDate(ContractEntity entity) {
        if (entity.getBillingStartDate().isBefore(entity.getInitialDate())) {
            throw new NotificationException("Data inicio de cobrança não pode ser anterior a data inicial.");
        }
    }

    private void avaliableToClient(ClientEntity entity) {
        if (entity.isNew() && repository.existsClientsInProgress(Situation.ONGOING, entity.getId())) {
            throw new NotificationException("Esse cliente já tem um contrato em andamento.");
        }
    }

    private void canTerminate(ContractEntity entity) {
        if (Situation.CLOSED.equals(entity.getSituation())) {
            if (entity.getReason() == null || entity.getReason().isEmpty()) {
                throw new NotificationException("É necessário informar um motivo para o encerramento de contrato.");
            }
        }
    }

    private void depositAmount(ContractEntity entity) {
        if (entity.getDepositAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new NotificationException("Valor caução não pode ser menor que zero.");
        }
        if (entity.getDepositAmount().compareTo(BigDecimal.ZERO) == 0) {
            throw new NotificationException("Valor caução não pode ser zero.");
        }
    }

    private void recurrenceValue(ContractEntity entity) {
        if (entity.getRecurrenceValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new NotificationException("Valor recorrência não pode ser menor que zero.");
        }
        if (entity.getRecurrenceValue().compareTo(BigDecimal.ZERO) == 0) {
            throw new NotificationException("Valor recorrência não pode ser zero.");
        }
    }

    @Override
    public List<ContractEntity> filterBy(String search) {
        if (search == null || search.isEmpty()) return repository.findAll();
        return repository.findBy(search);
    }
}
