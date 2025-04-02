package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class InvoiceServiceImpl extends BaseServiceImpl<TransactionEntity, Long> implements InvoiceService {

    @Autowired
    private TransactionRepository repository;

    public InvoiceServiceImpl(TransactionRepository repository) {
        super(repository);
    }

    @Override
    public TransactionEntity update(TransactionEntity invoice, TransactionEntity entity) {
        TransactionEntity savedEntity = findById(entity.getId());

        entity.setSignature(savedEntity.getSignature());
        invoice.setValue(calculateUpdatedValue(invoice.getValue(), savedEntity.getValue(), entity.getValue()));

        repository.save(invoice);
        return repository.save(entity);
    }

    @Override
    public List<TransactionEntity> searchById(Long id) {
        return repository.searchInvoiceById(id);
    }

    private BigDecimal calculateUpdatedValue(BigDecimal invoiceValue, BigDecimal previousValue, BigDecimal newValue) {
        return invoiceValue.subtract(previousValue).add(newValue);
    }
}
