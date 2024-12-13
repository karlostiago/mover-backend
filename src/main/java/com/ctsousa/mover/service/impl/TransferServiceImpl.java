package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.mapper.Transform;
import com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.response.TransactionResponse;
import com.ctsousa.mover.service.AccountService;
import com.ctsousa.mover.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;

@Component
public class TransferServiceImpl extends BaseTransactionServiceImpl implements TransferService {

    @Autowired
    private final AccountService accountService;

    public TransferServiceImpl(TransactionRepository repository, AccountService accountService) {
        super(repository, accountService);
        this.accountService = accountService;
    }

    @Override
    public TransactionEntity betweenAccount(Transaction transaction) {
        AccountEntity creditAccount = accountService.findById(transaction.getDestinationAccount().getId());
        AccountEntity debitAccount = accountService.findById(transaction.getAccount().getId());

        TransactionEntity entity = transaction.toEntity();

        entity.setTransactionType(TransactionType.CREDIT.name());
        entity.setAccount(creditAccount);
        repository.save(entity);

        if (entity.getPaid()) {
            updateBalance(creditAccount, entity.getValue());
        }

        entity.setTransactionType(TransactionType.DEBIT.name());
        entity.setAccount(debitAccount);
        entity.setValue(invertSignal(entity.getValue()));
        repository.save(entity);

        if (entity.getPaid()) {
            updateBalance(debitAccount, entity.getValue());
        }

        return entity;
    }

    @Override
    public TransactionEntity update(Transaction transaction) {
        TransactionEntity entity = findById(transaction.getId());
        List<TransactionEntity> entities = repository.findBySignature(entity.getSignature());
        AccountEntity creditAccount = accountService.findById(transaction.getDestinationAccount().getId());
        AccountEntity debitAccount = accountService.findById(transaction.getAccount().getId());

        for (TransactionEntity e : entities) {
            updateTransaction(e, transaction, debitAccount, creditAccount);
            updateBalance(entity, e, transaction.getValue());
        }

        return entity;
    }

    @Override
    public TransactionResponse searchById(Long id) {
        TransactionEntity transactionEntity = findById(id);
        List<TransactionEntity> entities = repository.findBySignature(transactionEntity.getSignature());
        TransactionResponse response = Transform.toMapper(transactionEntity, TransactionResponse.class);

        for (TransactionEntity entity : entities) {
            if (isDebit(entity)) {
                response.setValue(invertSignal(entity.getValue()));
                response.setAccountId(entity.getAccount().getId());
            } else {
                response.setDestinationAccountId(entity.getAccount().getId());
            }
        }

        return response;
    }

    private void updateBalance(TransactionEntity originalTransaction, TransactionEntity updatedTransaction, BigDecimal balance) {
        boolean wasPaid = originalTransaction.getPaid();
        boolean isNowPaid = updatedTransaction.getPaid();
        if (!wasPaid && isNowPaid) {
            updateBalance(updatedTransaction.getAccount(), updatedTransaction.getValue());
        } else if (wasPaid && !isNowPaid) {
            var value = isDebit(updatedTransaction) ? balance : invertSignal(balance);
            updateBalance(updatedTransaction.getAccount(), value);
        }
    }

    private void updateTransaction(TransactionEntity entity, Transaction transaction, AccountEntity debitAccount, AccountEntity creditAccount) {
        entity.setDescription(transaction.getDescription());
        entity.setDueDate(transaction.getDueDate());
        entity.setPaymentDate(transaction.getPaymentDate());
        entity.setPaid(transaction.getPaid());

        if (isDebit(entity)) {
            entity.setAccount(debitAccount);
            entity.setValue(invertSignal(transaction.getValue()));
        } else {
            entity.setAccount(creditAccount);
            entity.setValue(transaction.getValue());
        }

        repository.save(entity);
    }

    private Boolean isDebit(TransactionEntity entity) {
        return entity.getTransactionType().equals("DEBIT");
    }
}
