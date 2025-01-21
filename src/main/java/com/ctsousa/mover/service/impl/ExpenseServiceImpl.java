package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.ExpenseService;
import com.ctsousa.mover.service.FixedInstallmentService;
import com.ctsousa.mover.service.InstallmentService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExpenseServiceImpl extends BaseTransactionServiceImpl implements ExpenseService {

    public ExpenseServiceImpl(TransactionRepository repository, InstallmentService installmentService, FixedInstallmentService fixedInstallmentService) {
        super(repository, installmentService, fixedInstallmentService);
    }

    public void batchDelete(Long id) {
        TransactionEntity entity = findById(id);
        List<TransactionEntity> entities = repository.findBySignature(entity.getSignature())
                .stream().filter(t -> t.getInstallment() >= entity.getInstallment())
                .toList();

        Map<AccountEntity, BigDecimal> accumulatedBalance = entities.stream()
                .filter(TransactionEntity::getPaid)
                .collect(Collectors.groupingBy(
                        TransactionEntity::getAccount,
                        Collectors.reducing(BigDecimal.ZERO, TransactionEntity::getValue, BigDecimal::add)
                ));

        accumulatedBalance.forEach((account, balance) -> updateAccountBalance(account, account.getAvailableBalance().subtract(balance.abs())));

        repository.deleteAll(entities);
    }
}
