package com.ctsousa.mover.scheduler;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.SnapshotBalanceEntity;
import com.ctsousa.mover.repository.SnapshotBalanceRepository;
import com.ctsousa.mover.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static com.ctsousa.mover.core.util.DateUtil.isFirstDayOfMonth;

@Slf4j
@Component
public class SnapshotBalanceScheduler implements Scheduler {

    private final AccountService accountService;
    private final SnapshotBalanceRepository snapshotBalanceRepository;

    public SnapshotBalanceScheduler(AccountService accountService, SnapshotBalanceRepository snapshotBalanceRepository) {
        this.accountService = accountService;
        this.snapshotBalanceRepository = snapshotBalanceRepository;
    }

    @Override
    @Scheduled(cron = "0 3 1 * * *") // Executa uma vez por mês no dia 1 as 03h00 da manhã
    public void process() {
        LocalDate initialDate = LocalDate.now();
        boolean isFirstDayOfMonth = isFirstDayOfMonth(initialDate);

        if (isFirstDayOfMonth) {
            LocalDate finalDate = initialDate.minusDays(1);
            List<AccountEntity> accounts = accountService.findAll().stream()
                    .filter(AccountEntity::getActive)
                    .toList();
            if (accounts.isEmpty()) return;

            for (AccountEntity account : accounts) {
                SnapshotBalanceEntity entity = new SnapshotBalanceEntity();
                entity.setAccount(account);
                entity.setPeriod(finalDate);
                entity.setBalance(account.getAvailableBalance());
                entity.setActive(true);
                snapshotBalanceRepository.save(entity);
            }
        }
    }
}
