package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.repository.AccountRepository;
import com.ctsousa.mover.repository.BalanceRepository;
import com.ctsousa.mover.response.BalanceResponse;
import com.ctsousa.mover.service.BalanceService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;
    private final AccountRepository accountRepository;

    public BalanceServiceImpl(BalanceRepository balanceRepository, AccountRepository accountRepository) {
        this.balanceRepository = balanceRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public BalanceResponse calculateBalances(List<Long> listAccountId, List<TransactionEntity> entities) {
        BalanceResponse response = new BalanceResponse();

        if (listAccountId.isEmpty()) {
            listAccountId = accountRepository.findAll()
                    .stream().map(AccountEntity::getId)
                    .toList();
        }

        if (!entities.isEmpty()) {
            BigDecimal expenseBalance = entities.stream().filter(t -> "EXPENSE".equals(t.getCategoryType()))
                    .map(TransactionEntity::getValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .abs();

            BigDecimal incomeBalance = entities.stream().filter(t -> "INCOME".equals(t.getCategoryType()))
                    .map(TransactionEntity::getValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            response.setIncome(incomeBalance);
            response.setExpense(expenseBalance);
            response.setGeneralBalance(incomeBalance.subtract(expenseBalance));
        }

        response.setCurrentAccount(balanceRepository.accountBalance(listAccountId));
        return response;
    }
}
