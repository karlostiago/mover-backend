package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.MaintenanceEntity;
import com.ctsousa.mover.core.service.BaseService;

import java.util.List;

public interface MaintenanceService extends BaseService<MaintenanceEntity, Long> {

    List<MaintenanceEntity> filterBy(String search);
}
