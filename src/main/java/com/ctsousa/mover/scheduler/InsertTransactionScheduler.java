package com.ctsousa.mover.scheduler;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.util.NumberUtil;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;

@Slf4j
@Component
public class InsertTransactionScheduler implements Scheduler {

    public static final Queue<List<TransactionEntity>> queue = new ConcurrentLinkedQueue<>();

    private final AccountService accountService;

    protected final TransactionRepository repository;

    public InsertTransactionScheduler(AccountService accountService, TransactionRepository repository) {
        this.accountService = accountService;
        this.repository = repository;
    }

    @Override
    @Scheduled(cron = "0/1 * * * * *")
    public void process() {

        if (queue.isEmpty()) return;

        log.info("Iniciado processamento de insert de lançamentos :: {} ", LocalDateTime.now());
        while (!queue.isEmpty()) {
            List<TransactionEntity> entities = queue.poll();
            entities.forEach(this::saveAndUpdateBalance);
        }
        log.info("Finalizado processamento de insert de lançamentos :: {} ", LocalDateTime.now());
    }

    private void saveAndUpdateBalance(TransactionEntity entity) {
//        if(entity.getPaid()) {
//            updateBalance(entity.getAccount(), entity.getValue());
//        }

        repository.save(entity);
    }

//    private void updateBalance(final AccountEntity account, BigDecimal value) {
//        AccountEntity accountFound = accountService.findById(account.getId());
//        BigDecimal balance = accountFound.getAvailableBalance().add(value);
//        accountFound.setAvailableBalance(balance);
//        accountService.save(accountFound);
//    }
}
