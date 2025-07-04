package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.event.TransactionCacheEvent;
import com.ctsousa.mover.core.util.DateUtil;
import com.ctsousa.mover.domain.DashboardCache;
import com.ctsousa.mover.response.CardDashboardResponse;
import com.ctsousa.mover.response.ChartDoughnutResponse;
import com.ctsousa.mover.service.DashboardCacheLoaderService;
import com.ctsousa.mover.service.DashboardService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class DashboardServiceImpl implements DashboardService, ApplicationListener<ApplicationReadyEvent> {

    private final DashboardCacheLoaderService dashboardCacheLoaderService;

    private final LocalDate dtInitial;
    private final LocalDate dtFinal;

    private volatile DashboardCache dashboardCache;

    public DashboardServiceImpl(DashboardCacheLoaderService dashboardCacheLoaderService) {
        this.dashboardCacheLoaderService = dashboardCacheLoaderService;
        this.dtInitial = DateUtil.getFirstDay(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());
        this.dtFinal = DateUtil.getLastDay(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        dashboardCacheLoaderService.loadAsync(dtInitial, dtFinal).thenAccept(cache -> {
                    log.info("Dashboard cache carregado com sucesso.");
                    this.dashboardCache = cache;
                })
                .exceptionally(ex -> {
                    log.error("Erro ao carregar o dashboard cache", ex);
                    return null;
                });
    }

    @Override
    public CardDashboardResponse activeContracts() {
        return getSafeCache().getSummary().getOtherCard("activeContracts");
    }

    @Override
    public CardDashboardResponse terminatedContracts() {
        return getSafeCache().getSummary().getOtherCard("terminatedContracts");
    }

    @Override
    public CardDashboardResponse rentalVehicles() {
        return getSafeCache().getSummary().getOtherCard("rentalVehicles");
    }

    @Override
    public CardDashboardResponse stoppedVehicles() {
        return getSafeCache().getSummary().getOtherCard("stoppedVehicles");
    }

    @Override
    public CardDashboardResponse overdueRevenue() {
        return getSafeCache().getSummary().getRevenueCard("overdueRevenue");
    }

    @Override
    public CardDashboardResponse realizedRevenue() {
        return getSafeCache().getSummary().getRevenueCard("realizedRevenue");
    }

    @Override
    public CardDashboardResponse pendingRevenue() {
        return getSafeCache().getSummary().getRevenueCard("pendingRevenue");
    }

    @Override
    public CardDashboardResponse grossRevenue() {
        return getSafeCache().getSummary().getRevenueCard("grossRevenue");
    }

    @Override
    public CardDashboardResponse overdueExpense() {
        return getSafeCache().getSummary().getExpenseCard("overdueExpense");
    }

    @Override
    public CardDashboardResponse realizedExpense() {
        return getSafeCache().getSummary().getExpenseCard("realizedExpense");
    }

    @Override
    public CardDashboardResponse pendingExpense() {
        return getSafeCache().getSummary().getExpenseCard("pendingExpense");
    }

    @Override
    public CardDashboardResponse grossExpense() {
        return getSafeCache().getSummary().getExpenseCard("grossExpense");
    }

    @Override
    public List<CardDashboardResponse> balanceAccounts() {
        return getSafeCache().getSummary().getAccountBalances();
    }

    @Override
    public List<CardDashboardResponse> invoices() {
        return getSafeCache().getSummary().getInvoices();
    }

    @Override
    public ChartDoughnutResponse recipeChartCategory() {
        return getSafeCache().getSummary().getRevenueChart();
    }

    @Override
    public ChartDoughnutResponse expenseChartCategory() {
        return getSafeCache().getSummary().getExpenseChart();
    }

    @Override
    @EventListener
    public void handleTransactionCacheEvent(TransactionCacheEvent event) {
        updateCache();
    }

    private void updateCache() {
        try {
            this.dashboardCache = dashboardCacheLoaderService.load(dtInitial, dtFinal);
            log.info("Dashboard atualizado com sucesso.");
        } catch (Exception ex) {
            log.error("Erro ao atualizar dashboard", ex);
        }
    }

    private DashboardCache getSafeCache() {
        if (dashboardCache == null) {
            log.warn("Dashboard cache ainda não carregado. Carregando sincronicamente como fallback.");
            updateCache();
        }
        return dashboardCache;
    }
}
