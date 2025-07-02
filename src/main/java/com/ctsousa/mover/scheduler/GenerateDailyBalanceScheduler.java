package com.ctsousa.mover.scheduler;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.CardEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.repository.AccountRepository;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.BalanceService;
import com.ctsousa.mover.service.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GenerateDailyBalanceScheduler implements Scheduler {

    protected final TransactionRepository repository;
    private final BalanceService balanceService;
    private final AccountRepository accountRepository;

    public GenerateDailyBalanceScheduler(TransactionRepository repository, BalanceService balanceService, AccountRepository accountRepository) {
        this.repository = repository;
        this.balanceService = balanceService;
        this.accountRepository = accountRepository;
    }

    @Override
//    @Scheduled(fixedDelay = 10000L)
    public void process() {
        List<AccountEntity> entities = accountRepository.findAll();


        System.out.println("");
    }

    private Map<Map.Entry<LocalDate, CardEntity>, List<TransactionEntity>> groupByDueDateAndCard(List<TransactionEntity> entities) {
        return entities.stream().collect(Collectors.groupingBy(
                    t -> Map.entry(t.getDueDate(), t.getCard())
                ));
    }

    private List<TransactionEntity> getCardTransactionOnly(List<TransactionEntity> entities) {
        return Optional.of(entities)
                .orElse(Collections.emptyList())
                .stream()
                .filter(t -> t.getCard() != null && !t.getInvoice())
                .toList();
    }
}
