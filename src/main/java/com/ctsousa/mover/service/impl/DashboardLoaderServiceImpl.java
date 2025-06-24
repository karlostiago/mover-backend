package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.*;
import com.ctsousa.mover.domain.DashboardCache;
import com.ctsousa.mover.domain.DashboardSummary;
import com.ctsousa.mover.enumeration.Icon;
import com.ctsousa.mover.enumeration.Situation;
import com.ctsousa.mover.repository.ContractRepository;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.repository.VehicleRepository;
import com.ctsousa.mover.response.CardDashboardResponse;
import com.ctsousa.mover.service.AccountService;
import com.ctsousa.mover.service.CardService;
import com.ctsousa.mover.service.DashboardCacheLoaderService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class DashboardLoaderServiceImpl implements DashboardCacheLoaderService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final CardService cardService;
    private final ContractRepository contractRepository;
    private final VehicleRepository vehicleRepository;

    public DashboardLoaderServiceImpl(TransactionRepository transactionRepository, AccountService accountService, CardService cardService, ContractRepository contractRepository, VehicleRepository vehicleRepository) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.cardService = cardService;
        this.contractRepository = contractRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable("dashboardData")
    public DashboardCache load(LocalDate dtInitial, LocalDate dtFinal) {
        List<Long> ids = new ArrayList<>();
        int pageNumber = 0, pageSize = 100;
        Page<Long> page;

        do {
            page = transactionRepository.findByPeriod(dtInitial, dtFinal, PageRequest.of(pageNumber++, pageSize));
            ids.addAll(page.getContent());
        } while (page.hasNext());

        List<TransactionEntity> transactions = ids.isEmpty() ? Collections.emptyList() : transactionRepository.findByIdInWithDetails(ids);

        List<AccountEntity> accounts = accountService.findAll();
        List<CardDashboardResponse> invoices = getInvoices(dtInitial, dtFinal);

        DashboardSummary dashboardSummary = new DashboardSummary(transactions, accounts, invoices);
        dashboardSummary.addOtherCard("activeContracts", getActiveContracts());
        dashboardSummary.addOtherCard("terminatedContracts", getTerminatedContracts());
        dashboardSummary.addOtherCard("rentalVehicles", getRentalVehicles());
        dashboardSummary.addOtherCard("stoppedVehicles", getStoppedVehicles());

        return new DashboardCache(dashboardSummary);
    }

    private CardDashboardResponse getActiveContracts() {
        List<ContractEntity> entities = contractRepository.findBy(Situation.ONGOING);
        return CardDashboardResponse.builder()
                .loading(true)
                .quantity(entities.size())
                .build();
    }

    private CardDashboardResponse getTerminatedContracts() {
        List<ContractEntity> entities = contractRepository.findBy(Situation.CLOSED);
        return CardDashboardResponse.builder()
                .loading(true)
                .quantity(entities.size())
                .build();
    }

    private CardDashboardResponse getRentalVehicles() {
        List<VehicleEntity> allEntities = vehicleRepository.findAll();
        List<VehicleEntity> avaliabbleEntities = vehicleRepository.onlyAvailable();
        return CardDashboardResponse.builder()
                .loading(true)
                .quantity(allEntities.size() - avaliabbleEntities.size())
                .build();
    }

    private CardDashboardResponse getStoppedVehicles() {
        return CardDashboardResponse.builder()
                .loading(true)
                .quantity( vehicleRepository.onlyAvailable().size())
                .build();
    }

    private List<CardDashboardResponse> getInvoices(LocalDate dtInitial, LocalDate dtFinal) {
        return cardService.findAll().stream()
                .filter(CardEntity::getActive)
                .map(card -> CardDashboardResponse.builder()
                        .description(card.getName())
                        .loading(true)
                        .iconPath(Icon.toName(card.getIcon()).getUrlImage())
                        .value(cardService.calculateInvoiceValue(card, dtInitial, dtFinal))
                        .build())
                .toList();
    }
}
