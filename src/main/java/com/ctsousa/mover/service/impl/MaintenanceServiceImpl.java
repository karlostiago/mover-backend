package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.MaintenanceEntity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.repository.MaintenanceRepository;
import com.ctsousa.mover.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MaintenanceServiceImpl extends BaseServiceImpl<MaintenanceEntity, Long> implements MaintenanceService {

    @Autowired
    private MaintenanceRepository repository;

    public MaintenanceServiceImpl(MaintenanceRepository repository) {
        super(repository);
    }

    @Override
    public MaintenanceEntity save(MaintenanceEntity entity) {
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

    @Override
    public List<MaintenanceEntity> filterBy(String search) {
        if (search == null || search.isEmpty()) return repository.findAll();
        return null;//repository.findBy(search);
    }
}
