package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.SnapshotBalanceEntity;
import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.repository.SnapshotBalanceRepository;
import com.ctsousa.mover.repository.AccountRepository;
import com.ctsousa.mover.repository.BalanceRepository;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.response.BalanceResponse;
import com.ctsousa.mover.response.ExpectedBalanceResponse;
import com.ctsousa.mover.service.BalanceService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.ctsousa.mover.core.util.DateUtil.isFutureDate;
import static com.ctsousa.mover.core.util.DateUtil.minusMonth;

@Component
public class BalanceServiceImpl implements BalanceService {
    private static final Map<String, List<ExpectedBalanceResponse>> expectedBalanceCache = new ConcurrentHashMap<>();

    private final BalanceRepository balanceRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final SnapshotBalanceRepository snapshotBalanceRepository;

    public BalanceServiceImpl(BalanceRepository balanceRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, SnapshotBalanceRepository snapshotBalanceRepository) {
        this.balanceRepository = balanceRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.snapshotBalanceRepository = snapshotBalanceRepository;
    }

    @Override
    public List<ExpectedBalanceResponse> calculateExpectedBalanceOnDay(List<Long> listAccountId, LocalDate initialDate, LocalDate finalDate) {
        List<AccountEntity> accounts = findAccounts(listAccountId);

        boolean isFutureDate = isFutureDate(initialDate);

        if (isFutureDate) {
            initialDate = LocalDate.now().withDayOfMonth(1);
        }

        String key = createKeyCache(accounts, initialDate, finalDate);

        if (expectedBalanceCache.containsKey(key)) {
            return expectedBalanceCache.get(key);
        }

        YearMonth targetMonth = YearMonth.from(finalDate);
        LocalDate monthStart = targetMonth.atDay(1);
        LocalDate monthEnd = targetMonth.atEndOfMonth();

        LocalDate previousInitialDate = minusMonth(initialDate, 1);
        LocalDate previousFinalDate = minusMonth(finalDate,1);

        List<TransactionEntity> transactions = findTransactions(initialDate, finalDate);

        Map<LocalDate, BigDecimal> dailySums = groupBalanceDay(transactions);

        BigDecimal balance =  isFutureDate
                ? accounts.stream().map(AccountEntity::getAvailableBalance).reduce(BigDecimal.ZERO, BigDecimal::add)
                : searchLastSnapshot(accounts, previousInitialDate, previousFinalDate).getBalance();

        List<ExpectedBalanceResponse> response = new ArrayList<>(dailySums.size());
        for (Map.Entry<LocalDate, BigDecimal> entry : dailySums.entrySet()) {
            LocalDate date = entry.getKey();
            balance = balance.add(entry.getValue());
            if (!date.isBefore(monthStart) && !date.isAfter(monthEnd)) {
                response.add(ExpectedBalanceResponse.builder()
                        .period(date)
                        .balance(balance)
                        .build());
            }
        }

        expectedBalanceCache.put(key, response);
        return response;
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

    private SnapshotBalanceEntity searchLastSnapshot(List<AccountEntity> accounts, LocalDate initialDate, LocalDate finalDate) {
        List<SnapshotBalanceEntity> snapshots = snapshotBalanceRepository.findBy(initialDate, finalDate, accounts);
        if (!snapshots.isEmpty()) {
            return snapshots.stream().peek(snap -> {
                        BigDecimal balance = snapshots.stream()
                                .map(SnapshotBalanceEntity::getBalance)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        snap.setBalance(balance);
                    })
                    .toList().get(0);
        }
        SnapshotBalanceEntity snapshot = new SnapshotBalanceEntity();
        snapshot.setBalance(accounts.stream()
                .map(AccountEntity::getAvailableBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return snapshot;
    }

    private List<TransactionEntity> findTransactions(LocalDate initialDate, LocalDate finalDate) {
        List<Long> ids = transactionRepository.findByPeriod(initialDate, finalDate);
        return transactionRepository.findByIdInWithDetails(ids);
    }

    private Map<LocalDate, BigDecimal> groupBalanceDay(List<TransactionEntity> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> Boolean.TRUE.equals(t.getPaid()) ? t.getPaymentDate() : t.getDueDate(),
                        TreeMap::new,
                        Collectors.reducing(BigDecimal.ZERO, this::adjustTransationValue, BigDecimal::add)
                ));
    }

    private BigDecimal adjustTransationValue(TransactionEntity entity) {
        BigDecimal value = entity.getValue().abs();
        return TransactionType.CREDIT.name().equalsIgnoreCase(entity.getTransactionType()) ? value : value.negate();
    }

    private String createKeyCache(List<AccountEntity> accounts, LocalDate initialDate, LocalDate finalDate) {
        return accounts.stream()
                .map(AccountEntity::getId)
                .map(String::valueOf)
                .collect(Collectors.joining()) + "_" + initialDate + "_" + finalDate;
    }
}
