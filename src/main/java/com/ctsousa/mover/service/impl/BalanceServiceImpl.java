package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountBalancePhotoEntity;
import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.DailyBalanceEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.repository.*;
import com.ctsousa.mover.response.BalanceResponse;
import com.ctsousa.mover.response.DailyBalanceResponse;
import com.ctsousa.mover.service.BalanceService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BalanceServiceImpl implements BalanceService {
    private final BalanceRepository balanceRepository;
    private final AccountRepository accountRepository;
    private final DailyBalanceRepository dailyBalanceRepository;
    private final TransactionRepository transactionRepository;
    private final AccountBalancePhotoRepository accountBalancePhotoRepository;

    public BalanceServiceImpl(BalanceRepository balanceRepository, AccountRepository accountRepository, DailyBalanceRepository dailyBalanceRepository, TransactionRepository transactionRepository, AccountBalancePhotoRepository accountBalancePhotoRepository) {
        this.balanceRepository = balanceRepository;
        this.accountRepository = accountRepository;
        this.dailyBalanceRepository = dailyBalanceRepository;
        this.transactionRepository = transactionRepository;
        this.accountBalancePhotoRepository = accountBalancePhotoRepository;
    }

    @Override
    public void updateDailyBalance(LocalDate period, TransactionEntity entity) {
        LocalDate periodFinal = period.with(TemporalAdjusters.lastDayOfMonth());
        List<DailyBalanceEntity> dailyBalances = dailyBalanceRepository.balances(period, periodFinal, List.of(entity.getAccount()));
        for (DailyBalanceEntity dailyBalance : dailyBalances) {
            calculateDailyBalance(dailyBalance, entity);
            dailyBalanceRepository.save(dailyBalance);
        }
    }

    @Override
    public List<DailyBalanceResponse> findDailyBalances(List<Long> listAccountId, LocalDate periodInitial, LocalDate periodFinal) {
        List<AccountEntity> accounts = accountRepository.findAll();
//        BigDecimal accountBalance;

        if (!listAccountId.isEmpty()) {
            accounts = accounts.stream()
                    .filter(account -> listAccountId.contains(account.getId()))
                    .toList();
        }
//
//        AccountBalancePhotoEntity accountBalancePhoto = accountBalancePhotoRepository.snapshot(periodInitial,  periodFinal)
//                .orElse(null);
//
//        if (accountBalancePhoto == null) {
//            accountBalance = accounts.stream()
//                    .map(AccountEntity::getAvailableBalance)
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//            accountBalancePhoto = new AccountBalancePhotoEntity();
//            accountBalancePhoto.setAccountBalance(accountBalance);
//            accountBalancePhoto.setPeriod(periodFinal);
//            accountBalancePhoto = accountBalancePhotoRepository.save(accountBalancePhoto);
//        } else {
//            accountBalance = accountBalancePhoto.getAccountBalance();
//        }
//
//        List<Long> ids = transactionRepository.findByPeriod(periodInitial, periodFinal);
//        List<TransactionEntity> transactions = transactionRepository.findByIdInWithDetails(ids);
//
//        Map<LocalDate, List<TransactionEntity>> groupedTransactions = transactions.stream()
//                .collect(Collectors.groupingBy(
//                    t -> Boolean.TRUE.equals(t.getPaid()) ? t.getPaymentDate() : t.getDueDate(),
//                    TreeMap::new,
//                    Collectors.toList()
//                ));
//
//        List<DailyBalanceResponse> responses = new ArrayList<>();
//
//        for (Map.Entry<LocalDate, List<TransactionEntity>> entry : groupedTransactions.entrySet()) {
//            LocalDate period = entry.getKey();
//            BigDecimal balanceTransaction = entry.getValue().stream()
//                    .map(TransactionEntity::getValue)
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//            accountBalance = balanceTransaction.add(accountBalance);
//
//            DailyBalanceResponse response = new DailyBalanceResponse();
//            response.setPeriod(period);
//            response.setBalance(accountBalance);
//            responses.add(response);
//        }

        List<DailyBalanceEntity> entities = dailyBalanceRepository.balances(periodInitial, periodFinal, accounts);

        if (entities.isEmpty()) {
            var onlyAccountsActive = accounts.stream()
                    .filter(acc -> acc.getActive() && !acc.getCaution())
                    .toList();

            var currentDate = periodInitial;
            for (AccountEntity acc : onlyAccountsActive) {
                while(!currentDate.isAfter(periodFinal)) {
                    DailyBalanceEntity balance = new DailyBalanceEntity();
                    balance.setAccount(acc);
                    balance.setPeriod(currentDate);
                    balance.setBalance(acc.getAvailableBalance());
                    dailyBalanceRepository.save(balance);
                    currentDate = currentDate.plusDays(1);
                }
                currentDate = periodInitial;
            }

            entities = dailyBalanceRepository.balances(periodInitial, periodFinal, accounts);
        }

        Map<LocalDate, BigDecimal> groupedDailyBalance = groupDailyBalanceByPeriod(entities);

        int DAYS_IN_MONTH = 31;
        List<DailyBalanceResponse> responses = new ArrayList<>(DAYS_IN_MONTH);
        for (Map.Entry<LocalDate, BigDecimal> entry : groupedDailyBalance.entrySet()) {
            DailyBalanceResponse response = new DailyBalanceResponse();
            response.setBalance(entry.getValue());
            response.setPeriod(entry.getKey());
            responses.add(response);
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
        return "EXPENSE".equals(entity.getCategoryType())
                || "INVESTMENT".equals(entity.getCategoryType());
    }

    private boolean isIncome(TransactionEntity entity) {
        return "CORPORATE_CAPITAL".equals(entity.getCategoryType())
                || "INCOME".equals(entity.getCategoryType());
    }

    private Map<LocalDate, BigDecimal> groupDailyBalanceByPeriod(List<DailyBalanceEntity> entities) {
        return entities.stream()
                .collect(Collectors.groupingBy(
                        DailyBalanceEntity::getPeriod,
                        Collectors.mapping(
                                DailyBalanceEntity::getBalance,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));
    }

    private void calculateDailyBalance(DailyBalanceEntity dailyBalance, TransactionEntity entity) {
        BigDecimal value = entity.getValue().abs();
        if (TransactionType.CREDIT.name().equalsIgnoreCase(entity.getTransactionType())) {
            dailyBalance.setBalance(entity.getPaid() ? dailyBalance.getBalance().add(value)
                    : dailyBalance.getBalance().subtract(value));
        } else {
            dailyBalance.setBalance(entity.getPaid() ? dailyBalance.getBalance().subtract(value)
                    : dailyBalance.getBalance().add(value));
        }
    }

    private void calculateDailyBalance(DailyBalanceResponse dailyBalance, TransactionEntity entity) {
        BigDecimal value = entity.getValue().abs();
        if (TransactionType.CREDIT.name().equalsIgnoreCase(entity.getTransactionType())) {
            dailyBalance.setBalance(entity.getPaid() ? dailyBalance.getBalance().add(value)
                    : dailyBalance.getBalance().subtract(value));
        } else {
            dailyBalance.setBalance(entity.getPaid() ? dailyBalance.getBalance().subtract(value)
                    : dailyBalance.getBalance().add(value));
        }
    }
}
