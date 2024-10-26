package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.entity.ContractEntity;
import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.enumeration.Situation;
import com.ctsousa.mover.repository.ClientRepository;
import com.ctsousa.mover.repository.ContractRepository;
import com.ctsousa.mover.repository.VehicleRepository;
import com.ctsousa.mover.service.ClientService;
import com.ctsousa.mover.service.ContractService;
import com.ctsousa.mover.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class ContractServiceImpl extends BaseServiceImpl<ContractEntity, Long> implements ContractService {

    @Autowired
    private ContractRepository repository;

    private final ClientRepository clientRepository;

    private final VehicleRepository vehicleRepository;

    private final VehicleService vehicleService;

    private final ClientService clientService;

    public ContractServiceImpl(ContractRepository repository, ClientRepository clientRepository, VehicleRepository vehicleRepository, VehicleService vehicleService, ClientService clientService) {
        super(repository);
        this.clientRepository = clientRepository;
        this.vehicleRepository = vehicleRepository;
        this.vehicleService = vehicleService;
        this.clientService = clientService;
    }

    @Override
    @Transactional
    public ContractEntity save(ContractEntity entity) {
        validContract(entity);
        if (entity.isNew()) {
            if (repository.existsByNumber(entity.getNumber())) {
                throw new NotificationException("Ocorreu um erro ao salvar o contrato. Numeração em duplicidade!", Severity.ERROR);
            }
            vehicleRepository.updateSituation(entity.getVehicle().getId(), Situation.IN_FLEET.getDescription());
        } else {
            if (Situation.CLOSED.equals(entity.getSituation())) {
                return close(entity);
            }
        }
        return super.save(entity);
    }

    @Override
    public ContractEntity close(ContractEntity entity) {
        entity.setVehicle(vehicleService.findById(entity.getVehicle().getId()));
        VehicleEntity vehicleEntity = entity.getVehicle();
        vehicleEntity.setSituation(Situation.AVAILABLE.getDescription());
        vehicleRepository.save(vehicleEntity);

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

    private void validContract(ContractEntity entity) {
        avaliableToClient(entity.getClient());
        canTerminate(entity);
        billingStartDate(entity);
        initialDate(entity);
    }

    private void initialDate(ContractEntity entity) {
        if (entity.getInitialDate().isBefore(LocalDate.now())) {
            throw new NotificationException("Data inicial não pode ser anterior a data atual.");
        }
    }

    private void billingStartDate(ContractEntity entity) {
        if (entity.getBillingStartDate().isBefore(LocalDate.now())) {
            throw new NotificationException("Data inicio de cobrança não pode ser anterior a data atual.");
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

    @Override
    public List<ContractEntity> filterBy(String search) {
        if (search == null || search.isEmpty()) return repository.findAll();
        return repository.findBy(search);
    }
}
