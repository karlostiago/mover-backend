package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.repository.VehicleRepository;
import com.ctsousa.mover.service.VehicleService;

public class VehicleServiceImpl extends BaseServiceImpl<VehicleEntity, Long> implements VehicleService {

    private VehicleRepository repository;

    public VehicleServiceImpl(VehicleRepository repository) {
        super(repository);
    }
}
