package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.ContractEntity;
import com.ctsousa.mover.core.service.BaseService;

import java.util.List;

public interface ContractService extends BaseService<ContractEntity, Long> {

    List<ContractEntity> filterBy(String search);

    ContractEntity close(ContractEntity entity);
}
