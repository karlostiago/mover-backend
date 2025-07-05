package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.response.BalanceResponse;
import com.ctsousa.mover.response.ExpectedBalanceResponse;

import java.time.LocalDate;
import java.util.List;

public interface BalanceService  {

    BalanceResponse calculateBalances(final List<Long> listAccountId, final List<TransactionEntity> entities);

    List<ExpectedBalanceResponse> calculateExpectedBalanceOnDay(final List<Long> listAccountId, LocalDate initialDate, LocalDate finalDate);
}
