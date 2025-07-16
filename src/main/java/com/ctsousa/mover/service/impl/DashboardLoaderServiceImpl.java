package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.InvoiceProjection;
import com.ctsousa.mover.core.entity.*;
import com.ctsousa.mover.domain.DashboardCache;
import com.ctsousa.mover.domain.DashboardSummary;
import com.ctsousa.mover.enumeration.Icon;
import com.ctsousa.mover.enumeration.Situation;
import com.ctsousa.mover.response.CardDashboardResponse;
import com.ctsousa.mover.service.DashboardCacheLoaderService;
import com.ctsousa.mover.service.DashboardDataReaderService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class DashboardLoaderServiceImpl implements DashboardCacheLoaderService {

    private final DashboardDataReaderService dashboardDataReaderService;

    public DashboardLoaderServiceImpl(DashboardDataReaderService dashboardDataReaderService) {
        this.dashboardDataReaderService = dashboardDataReaderService;
    }

    @Async
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public CompletableFuture<DashboardCache> loadAsync(LocalDate dtInitial, LocalDate dtFinal) {
        DashboardCache cache = loadWithoutCache(dtInitial, dtFinal);
        return CompletableFuture.completedFuture(cache);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public DashboardCache load(LocalDate dtInitial, LocalDate dtFinal) {
        return loadWithoutCache(dtInitial, dtFinal);
    }

    private DashboardCache loadWithoutCache(LocalDate dtInitial, LocalDate dtFinal) {
        List<Long> ids = dashboardDataReaderService.findAllTransactionIds(dtInitial, dtFinal);
        List<TransactionEntity> transactions = dashboardDataReaderService.findAllTransactions(ids);

        List<AccountEntity> accounts = dashboardDataReaderService.findAllAccounts();
        List<CardDashboardResponse> invoices = getInvoices(dtInitial, dtFinal);

        DashboardSummary dashboardSummary = new DashboardSummary(transactions, accounts, invoices);
        dashboardSummary.addOtherCard("activeContracts", getActiveContracts());
        dashboardSummary.addOtherCard("terminatedContracts", getTerminatedContracts());
        dashboardSummary.addOtherCard("rentalVehicles", getRentalVehicles());
        dashboardSummary.addOtherCard("stoppedVehicles", getStoppedVehicles());

        return new DashboardCache(dashboardSummary);
    }

    private CardDashboardResponse getActiveContracts() {
        List<ContractEntity> entities = dashboardDataReaderService.findContractBy(Situation.ONGOING);
        return CardDashboardResponse.builder()
                .loading(true)
                .quantity(entities.size())
                .build();
    }

    private CardDashboardResponse getTerminatedContracts() {
        List<ContractEntity> entities = dashboardDataReaderService.findContractBy(Situation.CLOSED);
        return CardDashboardResponse.builder()
                .loading(true)
                .quantity(entities.size())
                .build();
    }

    private CardDashboardResponse getRentalVehicles() {
        List<VehicleEntity> allEntities = dashboardDataReaderService.findAllVehicle();
        List<VehicleEntity> avaliabbleEntities = dashboardDataReaderService.onlyVehicleAvailable();
        return CardDashboardResponse.builder()
                .loading(true)
                .quantity(allEntities.size() - avaliabbleEntities.size())
                .build();
    }

    private CardDashboardResponse getStoppedVehicles() {
        return CardDashboardResponse.builder()
                .loading(true)
                .quantity(dashboardDataReaderService.onlyVehicleAvailable().size())
                .build();
    }

    private List<CardDashboardResponse> getInvoices(LocalDate dtInitial, LocalDate dtFinal) {
        List<CardEntity> cards = dashboardDataReaderService.findAllCards()
                .stream()
                .filter(CardEntity::getActive)
                .toList();

        boolean allPreviousPaid = dashboardDataReaderService.allPreviousInvoicesPaid(cards, dtInitial, dtFinal);

        List<CardDashboardResponse> response = new ArrayList<>(cards.size());
        for (CardEntity card : cards) {
            InvoiceProjection projection = dashboardDataReaderService.calculateInvoiceValue(card, dtInitial, dtFinal);

            if (projection.getPaid() == 1 && !allPreviousPaid) {
                projection = dashboardDataReaderService.calculateInvoiceValue(card, dtInitial.plusMonths(1), dtFinal.plusMonths(1));
            }

            response.add(CardDashboardResponse.builder()
                    .description(card.getName())
                    .loading(true)
                    .iconPath(Icon.toName(card.getIcon()).getUrlImage())
                    .value(projection.getValue())
                    .paid(projection.getPaid() == 1)
                    .dueDate(projection.getDueDate())
                    .build());
        }

        return response;
    }
}
