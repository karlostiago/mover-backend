package com.ctsousa.mover.service;

import com.ctsousa.mover.response.TransactionBalanceResponse;

import java.time.LocalDate;
import java.util.List;

public interface TransactionBalanceService {

    List<TransactionBalanceResponse> calculateBalances(final List<Long> listAccountId, LocalDate dtInitial, LocalDate dtFinal);
}
