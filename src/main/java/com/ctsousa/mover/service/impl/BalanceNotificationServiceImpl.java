package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.service.BalanceNotificationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class BalanceNotificationServiceImpl implements BalanceNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public BalanceNotificationServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void notifyBalanceChanged() {
        messagingTemplate.convertAndSend("/topic/balance-update", "UPDATED");
    }
}
