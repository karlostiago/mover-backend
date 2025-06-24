package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.event.TransactionCacheEvent;
import com.ctsousa.mover.core.util.DateUtil;
import com.ctsousa.mover.domain.DashboardCache;
import com.ctsousa.mover.response.CardDashboardResponse;
import com.ctsousa.mover.response.ChartDoughnutResponse;
import com.ctsousa.mover.service.DashboardCacheLoaderService;
import com.ctsousa.mover.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class DashboardServiceImpl implements DashboardService {

    private final DashboardCacheLoaderService dashboardCacheLoaderService;
    private final CacheManager cacheManager;

    private final LocalDate dtInitial;
    private final LocalDate dtFinal;

    private volatile DashboardCache dashboardCache;

    public DashboardServiceImpl(DashboardCacheLoaderService dashboardCacheLoaderService, CacheManager cacheManager) {
        this.dashboardCacheLoaderService = dashboardCacheLoaderService;
        this.cacheManager = cacheManager;
        this.dtInitial = DateUtil.getFirstDay(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());
        this.dtFinal = DateUtil.getLastDay(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());
        realoadCache();
    }

    @Override
    public CardDashboardResponse activeContracts() {
        return dashboardCache.getSummary().getOtherCard("activeContracts");
    }

    @Override
    public CardDashboardResponse terminatedContracts() {
        return dashboardCache.getSummary().getOtherCard("terminatedContracts");
    }

    @Override
    public CardDashboardResponse rentalVehicles() {
        return dashboardCache.getSummary().getOtherCard("rentalVehicles");
    }

    @Override
    public CardDashboardResponse stoppedVehicles() {
        return dashboardCache.getSummary().getOtherCard("stoppedVehicles");
    }

    @Override
    public CardDashboardResponse overdueRevenue() {
        return dashboardCache.getSummary().getRevenueCard("overdueRevenue");
    }

    @Override
    public CardDashboardResponse realizedRevenue() {
        return dashboardCache.getSummary().getRevenueCard("realizedRevenue");
    }

    @Override
    public CardDashboardResponse pendingRevenue() {
        return dashboardCache.getSummary().getRevenueCard("pendingRevenue");
    }

    @Override
    public CardDashboardResponse grossRevenue() {
        return dashboardCache.getSummary().getRevenueCard("grossRevenue");
    }

    @Override
    public CardDashboardResponse overdueExpense() {
        return dashboardCache.getSummary().getExpenseCard("overdueExpense");
    }

    @Override
    public CardDashboardResponse realizedExpense() {
        return dashboardCache.getSummary().getExpenseCard("realizedExpense");
    }

    @Override
    public CardDashboardResponse pendingExpense() {
        return dashboardCache.getSummary().getExpenseCard("pendingExpense");
    }

    @Override
    public CardDashboardResponse grossExpense() {
        return dashboardCache.getSummary().getExpenseCard("grossExpense");
    }

    @Override
    public List<CardDashboardResponse> balanceAccounts() {
        return dashboardCache.getSummary().getAccountBalances();
    }

    @Override
    public List<CardDashboardResponse> invoices() {
        return dashboardCache.getSummary().getInvoices();
    }

    @Override
    public ChartDoughnutResponse recipeChartCategory() {
        return dashboardCache.getSummary().getRevenueChart();
    }

    @Override
    public ChartDoughnutResponse expenseChartCategory() {
        return dashboardCache.getSummary().getExpenseChart();
    }

    @Override
    public void handleTransactionCacheEvent(TransactionCacheEvent event) {
        Objects.requireNonNull(cacheManager.getCache("dashboardData")).clear();
        realoadCache();
    }

    private void realoadCache() {
        dashboardCache = dashboardCacheLoaderService.load(dtInitial, dtFinal);
    }
}
