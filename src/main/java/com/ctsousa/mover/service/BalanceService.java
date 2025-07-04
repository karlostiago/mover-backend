package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.response.BalanceResponse;
import com.ctsousa.mover.response.DailyBalanceResponse;

import java.time.LocalDate;
import java.util.List;

public interface BalanceService  {

    BalanceResponse calculateBalances(final List<Long> listAccountId, final List<TransactionEntity> entities);

    List<DailyBalanceResponse> calculateExpectedBalanceOnDay(final List<Long> listAccountId, LocalDate periodInitial, LocalDate periodFinal);
}
