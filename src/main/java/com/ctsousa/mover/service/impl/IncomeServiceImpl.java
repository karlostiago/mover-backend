package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.FixedInstallmentService;
import com.ctsousa.mover.service.IncomeService;
import com.ctsousa.mover.service.InstallmentService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class IncomeServiceImpl extends BaseTransactionServiceImpl implements IncomeService {

    public IncomeServiceImpl(TransactionRepository repository, InstallmentService installmentService, FixedInstallmentService fixedInstallmentService) {
        super(repository, installmentService, fixedInstallmentService);
    }

    @Override
    public TransactionEntity filterById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotificationException("Transação não encontrada"));
    }

    public void delete(Long id) {
        TransactionEntity entity = findById(id);

        if (entity.getPaid()) {
            BigDecimal availableBalance = entity.getAccount().getAvailableBalance()
                    .subtract(entity.getValue());
            updateAccountBalance(entity.getAccount(), availableBalance);
        }

        repository.deleteById(id);
    }

    @Override
    protected void updateAvailableBalance(Transaction transaction, Long accountId) {
        if (isPaymentStatusChanged(transaction)) {
            AccountEntity account = accountService.findById(accountId);
            BigDecimal availableBalance = account.getAvailableBalance();
            if (transaction.getPaid()) {
                availableBalance = availableBalance.add(transaction.getValue());
            } else {
                availableBalance = availableBalance.subtract(transaction.getValue());
            }
            account.setAvailableBalance(availableBalance);
            accountService.save(account);
        }
    }
}
