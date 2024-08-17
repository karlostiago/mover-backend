package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.core.service.BaseService;

import java.util.List;

public interface VehicleService extends BaseService<VehicleEntity, Long> {

    List<VehicleEntity> findBy(String search);
}
