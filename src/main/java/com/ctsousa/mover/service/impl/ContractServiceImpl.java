package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.entity.ContractEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.enumeration.Situation;
import com.ctsousa.mover.repository.ClientRepository;
import com.ctsousa.mover.repository.ContractRepository;
import com.ctsousa.mover.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContractServiceImpl extends BaseServiceImpl<ContractEntity, Long> implements ContractService {

    @Autowired
    private ContractRepository repository;

    private ClientRepository clientRepository;


    public ContractServiceImpl(ContractRepository repository) {
        super(repository);
    }

    @Override
    public ContractEntity save(ContractEntity entity) {
        validContract(entity);
        if (entity.isNew()) {
//            if (accountRepository.existsByHash(entity.getHash())) {
//                throw new NotificationException("Já existe uma conta cadastrada, com os dados informados.", Severity.WARNING);
//            }
        } else if (!entity.isNew()) {
//            if (accountRepository.existsByNumberAndNameNotId(entity.getNumber(), entity.getName(), entity.getId())) {
//                throw new NotificationException("Não foi possível atualizar, já tem uma conta, com os dados informado.", Severity.WARNING);
//            }
        }
        return super.save(entity);
    }

    private void validContract(ContractEntity entity) {
        avaliableToClient(entity.getClient());
        canTerminate(entity);
    }

    private void avaliableToClient(ClientEntity entity) {
        if (repository.existsClientsInProgress(Situation.ONGOING, entity.getId())) {
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

    //    @Override
//    public List<ContractEntity> filterBy(String search) {
//        if (search == null || search.isEmpty()) return repository.findAll();
//        return repository.findBy(search);
//    }
}
