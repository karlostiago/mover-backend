package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.service.BaseService;

import java.util.List;

public interface InvoiceService extends BaseService<TransactionEntity, Long> {

    List<TransactionEntity> searchById(Long id);

    TransactionEntity update(TransactionEntity invoice, TransactionEntity entity);
}
