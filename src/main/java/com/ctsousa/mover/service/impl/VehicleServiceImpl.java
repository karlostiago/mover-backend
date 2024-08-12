package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.core.service.impl.AbstractServiceImpl;
import com.ctsousa.mover.service.VehicleService;
import org.springframework.data.jpa.repository.JpaRepository;

public class VehicleServiceImpl extends AbstractServiceImpl<VehicleEntity, Long> implements VehicleService {

    public VehicleServiceImpl(JpaRepository<VehicleEntity, Long> repository) {
        super(repository);
    }
}
