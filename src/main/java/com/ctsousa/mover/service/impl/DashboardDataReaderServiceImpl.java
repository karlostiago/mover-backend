package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.InvoiceProjection;
import com.ctsousa.mover.core.entity.*;
import com.ctsousa.mover.enumeration.Situation;
import com.ctsousa.mover.repository.*;
import com.ctsousa.mover.service.CardService;
import com.ctsousa.mover.service.DashboardDataReaderService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
public class DashboardDataReaderServiceImpl implements DashboardDataReaderService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CardService cardService;
    private final ContractRepository contractRepository;
    private final VehicleRepository vehicleRepository;
    private final BalanceRepository balanceRepository;
    private final InvoiceRepository invoiceRepository;

    public DashboardDataReaderServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository, CardService cardService, ContractRepository contractRepository, VehicleRepository vehicleRepository, BalanceRepository balanceRepository, InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.cardService = cardService;
        this.contractRepository = contractRepository;
        this.vehicleRepository = vehicleRepository;
        this.balanceRepository = balanceRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<Long> findAllTransactionIds(LocalDate dtInitial, LocalDate dtFinal) {
        return transactionRepository.findByPeriod(dtInitial, dtFinal);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<TransactionEntity> findAllTransactions(List<Long> ids) {
        return ids.isEmpty()
                ? Collections.emptyList()
                : transactionRepository.findByIdInWithDetails(ids);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<AccountEntity> findAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<CardEntity> findAllCards() {
        return cardService.findAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public InvoiceProjection calculateInvoiceValue(CardEntity entity, LocalDate dtInitial, LocalDate dtFinal) {
        return balanceRepository.invoiceValue(entity.getId(), dtInitial, dtFinal);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<ContractEntity> findContractBy(Situation situation) {
        return contractRepository.findBy(situation);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<VehicleEntity> findAllVehicle() {
        return vehicleRepository.findAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<VehicleEntity> onlyVehicleAvailable() {
        return vehicleRepository.onlyAvailable();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<TransactionEntity> findAllInvoices(LocalDate period, List<CardEntity> cards) {
        return invoiceRepository.findBy(period, cards);
    }
}
