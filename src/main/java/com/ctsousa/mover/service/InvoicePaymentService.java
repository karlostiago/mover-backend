package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.InvoicePaymentDetailEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface InvoicePaymentService {

    TransactionEntity create(TransactionEntity invoice, AccountEntity account, LocalDate paymentDate, BigDecimal value);

    Long findPaymentId(Long id);

    List<InvoicePaymentDetailEntity> findByPaymentDetails(Long invoiceId);
}
