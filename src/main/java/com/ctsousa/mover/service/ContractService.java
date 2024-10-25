package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.ContractEntity;
import com.ctsousa.mover.core.service.BaseService;

public interface ContractService extends BaseService<ContractEntity, Long> {

//    List<AccountEntity> filterBy(String search);
    ContractEntity close(ContractEntity entity);
}
