package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.FixedInstallmentService;
import com.ctsousa.mover.service.InstallmentService;
import com.ctsousa.mover.service.InvestimentService;
import org.springframework.stereotype.Component;

@Component
public class InvestimentServiceImpl extends BaseTransactionServiceImpl implements InvestimentService {

    public InvestimentServiceImpl(TransactionRepository repository, InstallmentService installmentService, FixedInstallmentService fixedInstallmentService) {
        super(repository, installmentService, fixedInstallmentService);
    }
}
