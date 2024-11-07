package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.MaintenanceEntity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.enumeration.TypeMaintenance;
import com.ctsousa.mover.repository.MaintenanceRepository;
import com.ctsousa.mover.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ctsousa.mover.core.util.DateUtil.isValidDate;
import static com.ctsousa.mover.core.util.DateUtil.parseToLocalDate;

@Component
public class MaintenanceServiceImpl extends BaseServiceImpl<MaintenanceEntity, Long> implements MaintenanceService {

    @Autowired
    private MaintenanceRepository repository;

    public MaintenanceServiceImpl(MaintenanceRepository repository) {
        super(repository);
    }

    @Override
    public MaintenanceEntity save(MaintenanceEntity entity) {
        return super.save(entity);
    }

    @Override
    public List<MaintenanceEntity> filterBy(String search) {
        if (search == null || search.isEmpty()) return repository.findAll();

        TypeMaintenance type = TypeMaintenance.fromQuery(search);

        if (type != null) {
            search = type.name();
        }

        if (isValidDate(search)) {
            return repository.findBy(parseToLocalDate(search));
        }

        return repository.findBy(search);
    }
}
