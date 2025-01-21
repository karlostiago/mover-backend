package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.CorporateCapitalService;
import com.ctsousa.mover.service.FixedInstallmentService;
import com.ctsousa.mover.service.InstallmentService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CorporateCapitalServiceImpl extends BaseTransactionServiceImpl implements CorporateCapitalService {

    public CorporateCapitalServiceImpl(TransactionRepository repository, InstallmentService installmentService, FixedInstallmentService fixedInstallmentService) {
        super(repository, installmentService, fixedInstallmentService);
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

    @Override
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

        accumulatedBalance.forEach((account, balance) -> updateAccountBalance(account, account.getAvailableBalance().subtract(balance)));

        repository.deleteAll(entities);
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
}
