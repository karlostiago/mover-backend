package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.*;
import com.ctsousa.mover.enumeration.Situation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface DashboardDataReaderService {

    List<Long> findAllTransactionIds(LocalDate dtInitial, LocalDate dtFinal);

    List<TransactionEntity> findAllTransactions(List<Long> ids);

    List<AccountEntity> findAllAccounts();

    List<CardEntity> findAllCards();

    BigDecimal calculateInvoiceValue(CardEntity entity, LocalDate dtInicial, LocalDate dtFinal);

    List<ContractEntity> findContractBy(Situation situation);

    List<VehicleEntity> findAllVehicle();

    List<VehicleEntity> onlyVehicleAvailable();
}
