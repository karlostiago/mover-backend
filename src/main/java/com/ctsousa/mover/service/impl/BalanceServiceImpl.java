package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountBalancePhotoEntity;
import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.repository.AccountBalancePhotoRepository;
import com.ctsousa.mover.repository.AccountRepository;
import com.ctsousa.mover.repository.BalanceRepository;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.response.BalanceResponse;
import com.ctsousa.mover.response.DailyBalanceResponse;
import com.ctsousa.mover.service.BalanceService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.ctsousa.mover.core.util.DateUtil.isFutureDate;
import static com.ctsousa.mover.core.util.DateUtil.minusMonth;

@Component
public class BalanceServiceImpl implements BalanceService {
    private final BalanceRepository balanceRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountBalancePhotoRepository accountBalancePhotoRepository;

    public BalanceServiceImpl(BalanceRepository balanceRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, AccountBalancePhotoRepository accountBalancePhotoRepository) {
        this.balanceRepository = balanceRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.accountBalancePhotoRepository = accountBalancePhotoRepository;
    }

    @Override
    public List<DailyBalanceResponse> calculateExpectedBalanceOnDay(List<Long> listAccountId, LocalDate periodInitial, LocalDate periodFinal) {
        List<AccountEntity> accounts = findAccounts(listAccountId);
        LocalDate today = LocalDate.now();
        boolean isFutureDate = isFutureDate(periodInitial);

        if (isFutureDate) {
            periodInitial = today.withDayOfMonth(1);
        }

        LocalDate previousInitialDate = minusMonth(periodInitial, 1);
        LocalDate previousFinalDate = minusMonth(periodFinal,1);

        AccountBalancePhotoEntity snapshot = findSnapshot(accounts, previousInitialDate, previousFinalDate);
        List<TransactionEntity> transactions = findTransactions(periodInitial, periodFinal);
        Map<LocalDate, List<TransactionEntity>> groupedTransactions = groupedTransactionsByPeriod(transactions);
        List<DailyBalanceResponse> responses = new ArrayList<>(31);
        BigDecimal balance = snapshot.getBalance();

        if (isFutureDate) {
            balance = accounts.stream()
                        .map(AccountEntity::getAvailableBalance)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        for (Map.Entry<LocalDate, List<TransactionEntity>> entry : groupedTransactions.entrySet()) {
            LocalDate date = entry.getKey();

            BigDecimal previousBalance = entry.getValue().stream()
                    .map(this::adjustTransationValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            balance = balance.add(previousBalance);

            DailyBalanceResponse dailyBalanceResponse = new DailyBalanceResponse();
            dailyBalanceResponse.setPeriod(date);
            dailyBalanceResponse.setBalance(balance);
            responses.add(dailyBalanceResponse);
        }

        return responses;
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
            BigDecimal expenseBalance = entities.stream().filter(this::isExpense)
                    .map(TransactionEntity::getValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .abs();

            BigDecimal incomeBalance = entities.stream().filter(this::isIncome)
                    .map(TransactionEntity::getValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            response.setIncome(incomeBalance);
            response.setExpense(expenseBalance);
            response.setGeneralBalance(incomeBalance.subtract(expenseBalance));
        }

        response.setCurrentAccount(balanceRepository.accountBalance(listAccountId));
        return response;
    }

    private boolean isExpense(TransactionEntity entity) {
        return TypeCategory.EXPENSE.name().equalsIgnoreCase(entity.getCategoryType())
                || TypeCategory.INVESTMENT.name().equalsIgnoreCase(entity.getCategoryType());
    }

    private boolean isIncome(TransactionEntity entity) {
        return TypeCategory.CORPORATE_CAPITAL.name().equalsIgnoreCase(entity.getCategoryType())
                || TypeCategory.INCOME.name().equalsIgnoreCase(entity.getCategoryType());
    }

    private List<AccountEntity> findAccounts(List<Long> listAccountId) {
        List<AccountEntity> accounts = accountRepository.findAll().stream()
                .filter(AccountEntity::getActive)
                .toList();

        if (accounts.isEmpty()) throw new NotificationException("Não foi encontrada nenhuma conta ativa.");

        if (!listAccountId.isEmpty()) {
            accounts = accounts.stream()
                    .filter(account -> listAccountId.contains(account.getId()))
                    .toList();
        }

        return accounts;
    }

    private AccountBalancePhotoEntity findSnapshot(List<AccountEntity> accounts, LocalDate periodInitial, LocalDate periodFinal) {
        List<AccountBalancePhotoEntity> snapshots = accountBalancePhotoRepository.snapshot(periodInitial, periodFinal, accounts);
        if (!snapshots.isEmpty()) {
            return snapshots.stream().peek(snap -> {
                        BigDecimal balance = snapshots.stream()
                                .map(AccountBalancePhotoEntity::getBalance)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        snap.setBalance(balance);
                    })
                    .toList().get(0);
        }
        AccountBalancePhotoEntity snapshot = new AccountBalancePhotoEntity();
        snapshot.setBalance(BigDecimal.ZERO);
        return snapshot;
    }

    private List<TransactionEntity> findTransactions(LocalDate periodInitial, LocalDate periodFinal) {
        List<Long> ids = transactionRepository.findByPeriod(periodInitial, periodFinal);
        return transactionRepository.findByIdInWithDetails(ids);
    }

    private Map<LocalDate, List<TransactionEntity>> groupedTransactionsByPeriod(List<TransactionEntity> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> Boolean.TRUE.equals(t.getPaid()) ? t.getPaymentDate() : t.getDueDate(),
                        TreeMap::new,
                        Collectors.toList()
                ));
    }

    private BigDecimal adjustTransationValue(TransactionEntity entity) {
        BigDecimal value = entity.getValue().abs();
        return TransactionType.CREDIT.name().equalsIgnoreCase(entity.getTransactionType()) ? value : value.negate();
    }
}
