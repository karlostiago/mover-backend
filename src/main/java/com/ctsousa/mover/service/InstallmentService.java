package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.domain.Transaction;

import java.util.List;

public interface InstallmentService  {

    List<TransactionEntity> generated(Transaction transaction);

    boolean hasInstallment(Transaction transaction);
}
