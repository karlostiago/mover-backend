package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.domain.Transaction;

import java.util.List;

public interface FixedInstallmentService {

    List<TransactionEntity> generated(Transaction transaction);

    boolean isFixed(Transaction transaction);
}
