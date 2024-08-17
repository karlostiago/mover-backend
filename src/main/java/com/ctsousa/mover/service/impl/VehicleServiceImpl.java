package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.repository.VehicleRepository;
import com.ctsousa.mover.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VehicleServiceImpl extends BaseServiceImpl<VehicleEntity, Long> implements VehicleService {

    @Autowired
    private VehicleRepository repository;

    public VehicleServiceImpl(VehicleRepository repository) {
        super(repository);
    }

    @Override
    public VehicleEntity save(VehicleEntity entity) {
        if (entity.isNew()) {
            if (repository.existsByLicensePlate(entity.getLicensePlate())) {
                throw new NotificationException("Já existe um veículo com a placa informada.");
            }
            if (repository.existsByRenavam(entity.getRenavam())) {
                throw new NotificationException("Já existe um veículo com renavam informada.");
            }
        } else if (!entity.isNew()) {
            if (repository.existsByLicensePlateOrRenavamNotId(entity.getRenavam(), entity.getLicensePlate(), entity.getId())) {
                throw new NotificationException("Não foi possível atualizar, pois já tem um veículo, com o renavam ou placa informado.", Severity.WARNING);
            }
        }
        return super.save(entity);
    }

    @Override
    public List<VehicleEntity> findBy(String search) {
        return repository.findBy(search, search);
    }
}
