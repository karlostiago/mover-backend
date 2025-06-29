package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.projection.TransactionBalanceProjection;
import com.ctsousa.mover.repository.AccountRepository;
import com.ctsousa.mover.repository.TransactionBalanceRepository;
import com.ctsousa.mover.response.TransactionBalanceResponse;
import com.ctsousa.mover.service.TransactionBalanceService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class TransactionBalanceServiceImpl implements TransactionBalanceService {

    private final TransactionBalanceRepository balanceRepository;
    private final AccountRepository accountRepository;

    public TransactionBalanceServiceImpl(TransactionBalanceRepository balanceRepository, AccountRepository accountRepository) {
        this.balanceRepository = balanceRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<TransactionBalanceResponse> calculateBalances(List<Long> listAccountId, LocalDate dtInitial, LocalDate dtFinal) {
        if (listAccountId.isEmpty()) {
            listAccountId = accountRepository.findAll()
                    .stream().map(AccountEntity::getId)
                    .toList();
        }

        List<TransactionBalanceProjection> balances = balanceRepository.balance(listAccountId, dtInitial, dtFinal);

        return Optional.ofNullable(balances)
                .orElseGet(List::of)
                .stream()
                .map(balance -> new TransactionBalanceResponse(balance.getDate(), balance.getBalance()))
                .toList();
    }
}
