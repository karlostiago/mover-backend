package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.TransactionEntity;

import java.util.List;

public interface InvoiceService {

    List<TransactionEntity> genereteInvoice(final List<TransactionEntity> entities);
}
