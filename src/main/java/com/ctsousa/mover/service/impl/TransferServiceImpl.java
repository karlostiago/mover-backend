package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.AccountService;
import com.ctsousa.mover.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static com.ctsousa.mover.core.util.NumberUtil.currencyFormatter;
import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;

@Component
public class TransferServiceImpl implements TransferService {

    @Autowired
    private AccountService accountService;

    @Override
    public TransactionEntity transferBetweenAccount(Transaction transaction, TransactionRepository repository) {
        AccountEntity creditAccount = accountService.findById(transaction.getDestinationAccount().getId());
        AccountEntity debitAccount = accountService.findById(transaction.getAccount().getId());

        TransactionEntity entity = transaction.toEntity();
        hasBalance(debitAccount, entity.getValue());

        entity.setTransactionType(TransactionType.CREDIT.name());
        entity.setAccount(creditAccount);
        repository.save(entity);

        if (entity.getPaid()) {
            updateBalance(creditAccount, entity.getValue());
        }

        entity.setTransactionType(TransactionType.DEBIT.name());
        entity.setAccount(debitAccount);
        entity.setValue(entity.getValue().multiply(BigDecimal.valueOf(-1)));
        repository.save(entity);

        if (entity.getPaid()) {
            updateBalance(debitAccount, entity.getValue());
        }

        return entity;
    }

    @Override
    public void pay(final String signature, TransactionRepository repository) {
        List<TransactionEntity> entities = repository.findBySignature(signature);

        for (TransactionEntity entity : entities) {
            if (entity.getPaid()) continue;

            entity.setPaid(true);
            entity.setPaymentDate(LocalDate.now());
            hasBalance(entity.getAccount(), entity.getValue());
            updateBalance(entity.getAccount(), entity.getValue());
            repository.save(entity);
        }
    }

    @Override
    public void refund(String signature, TransactionRepository repository) {
        List<TransactionEntity> entities = repository.findBySignature(signature);

        for (TransactionEntity entity : entities) {
            if (!entity.getPaid()) continue;
            entity.setPaid(false);
            entity.setRefund(true);
            entity.setPaymentDate(LocalDate.now());
            updateBalance(entity.getAccount(), invertSignal(entity.getValue()));
            repository.save(entity);
        }
    }

    private void updateBalance(AccountEntity account, BigDecimal value) {
        BigDecimal balance = account.getAvailableBalance()
                .add(value);
        account.setAvailableBalance(balance);
        accountService.save(account);
    }

    private void hasBalance(AccountEntity account, BigDecimal value) {
        if (account.getAvailableBalance().compareTo(value) < 0) {
            throw new NotificationException("Não foi possível completar essa transferência, por insuficiência de saldo. Essa conta só tem disponível "
                    + currencyFormatter(account.getAvailableBalance(), new Locale("pt", "BR")));
        }
    }
}
