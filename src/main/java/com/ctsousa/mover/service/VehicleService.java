package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.core.service.BaseService;

public interface VehicleService extends BaseService<VehicleEntity, Long> {

    VehicleEntity findByLicensePlate(String licensePlate);

    VehicleEntity findByRenavam(String renavam);
}
