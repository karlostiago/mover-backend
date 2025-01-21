package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.ExpenseService;
import com.ctsousa.mover.service.FixedInstallmentService;
import com.ctsousa.mover.service.InstallmentService;
import org.springframework.stereotype.Component;

@Component
public class ExpenseServiceImpl extends BaseTransactionServiceImpl implements ExpenseService {

    public ExpenseServiceImpl(TransactionRepository repository, InstallmentService installmentService, FixedInstallmentService fixedInstallmentService) {
        super(repository, installmentService, fixedInstallmentService);
    }
}
