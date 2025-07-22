package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.FineEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.service.BaseService;
import com.ctsousa.mover.domain.Transaction;

public interface FineService extends BaseService<FineEntity, Long> {

    FineEntity save(FineEntity entity, Transaction transaction);
}
