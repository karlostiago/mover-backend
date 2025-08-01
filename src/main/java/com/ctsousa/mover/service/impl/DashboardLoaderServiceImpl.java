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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
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
        List<CardDashboardResponse> invoices = getInvoices(dtFinal);

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

    private List<CardDashboardResponse> getInvoices(LocalDate period) {
        List<CardEntity> cards = dashboardDataReaderService.findAllCards()
                .stream()
                .filter(CardEntity::getActive)
                .toList();

        List<TransactionEntity> invoices = dashboardDataReaderService.findAllInvoices(period, cards)
                .stream()
                .filter(transaction -> transaction.getValue().compareTo(BigDecimal.ZERO) != 0)
                .toList();

        Map<CardEntity, List<TransactionEntity>> groupedByCard = new HashMap<>();
        for (TransactionEntity invoice : invoices) {
            groupedByCard.computeIfAbsent(invoice.getCard(), k -> new java.util.ArrayList<>()).add(invoice);
        }

        LocalDate lowestDueDate = getLowestDueDate(invoices);
        Map<CardEntity, TransactionEntity> invoiceMap = getGroupInvoicesEarliestDueDate(groupedByCard, lowestDueDate);

        boolean hasInvoices = !invoiceMap.isEmpty();
        List<CardDashboardResponse> responses = new ArrayList<>();

        if (hasInvoices) {
            for (CardEntity card : cards) {
                TransactionEntity invoice = invoiceMap.get(card);
                if (invoice == null) {
                    InvoiceProjection projection = dashboardDataReaderService
                            .calculateInvoiceValue(card, lowestDueDate, lowestDueDate.withDayOfMonth(lowestDueDate.lengthOfMonth()));
                    responses.add(buildCardResponse(card, projection.getValue(), projection.getPaid() == 1, projection.getDueDate()));
                } else {
                    responses.add(buildCardResponse(card, invoice.getValue(), invoice.getPaid(), invoice.getDueDate()));
                }
            }
        } else {
            for (CardEntity card : cards) {
                responses.add(buildCardResponse(card, BigDecimal.ZERO, Boolean.FALSE,
                        lowestDueDate == null ? LocalDate.now().plusMonths(1) : lowestDueDate));
            }
        }

        return responses;
    }

    private Map<CardEntity, TransactionEntity> getGroupInvoicesEarliestDueDate(Map<CardEntity, List<TransactionEntity>> groupedByCard, LocalDate lowestDueDate) {
        Map<CardEntity, TransactionEntity> invoiceMap = new HashMap<>();
        for (Map.Entry<CardEntity, List<TransactionEntity>> entry : groupedByCard.entrySet()) {
            CardEntity card = entry.getKey();
            List<TransactionEntity> entities = entry.getValue();
            for (TransactionEntity entity : entities) {
                if (card.equals(entity.getCard()) && entity.getDueDate().getMonth().equals(lowestDueDate.getMonth())) {
                    invoiceMap.put(card, entity);
                    break;
                }
            }
        }
        return invoiceMap;
    }

    private CardDashboardResponse buildCardResponse(CardEntity entity, BigDecimal value, Boolean paid, LocalDate dueDate) {
        return CardDashboardResponse.builder()
                .description(entity.getName())
                .loading(true)
                .iconPath(Icon.toName(entity.getIcon()).getUrlImage())
                .dueDate(dueDate)
                .paid(paid)
                .value(value)
                .build();
    }

    private LocalDate getLowestDueDate(List<TransactionEntity> invoices) {
        return invoices.stream()
                .map(TransactionEntity::getDueDate)
                .min(Comparator.naturalOrder())
                .orElse(null);
    }
}
