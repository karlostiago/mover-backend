package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.InvoicePaymentDetailEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.service.BaseService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface InvoicePaymentService extends BaseService<TransactionEntity, Long> {

    TransactionEntity create(TransactionEntity invoice, AccountEntity account, LocalDate paymentDate, BigDecimal value);

    Long findPaymentId(Long id);

    List<InvoicePaymentDetailEntity> findByPaymentDetails(Long invoiceId);

    void deletePaymentDetail(Long id);
}
