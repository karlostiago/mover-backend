package com.ctsousa.mover.service;

import org.springframework.stereotype.Service;

@Service
public interface BalanceNotificationService {
    void notifyBalanceChanged();
}
