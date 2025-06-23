package com.ctsousa.mover.listener;

import com.ctsousa.mover.core.event.BalanceChangedEvent;
import com.ctsousa.mover.service.AccountService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UpdateBalanceListener {

    private final AccountService accountService;

    public UpdateBalanceListener(AccountService accountService) {
        this.accountService = accountService;
    }

    @Async("taskExecutor")
    @EventListener
    public void balanceChanged(BalanceChangedEvent event) {
        accountService.recalculateBalance();
    }
}
