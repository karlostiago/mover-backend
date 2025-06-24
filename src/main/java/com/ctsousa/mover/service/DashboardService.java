package com.ctsousa.mover.service;

import com.ctsousa.mover.core.event.TransactionCacheEvent;
import com.ctsousa.mover.response.CardDashboardResponse;
import com.ctsousa.mover.response.ChartDoughnutResponse;

import java.util.List;

public interface DashboardService {
    CardDashboardResponse activeContracts();

    CardDashboardResponse terminatedContracts();

    CardDashboardResponse rentalVehicles();

    CardDashboardResponse stoppedVehicles();

    CardDashboardResponse overdueRevenue();

    CardDashboardResponse realizedRevenue();

    CardDashboardResponse pendingRevenue();

    CardDashboardResponse grossRevenue();

    CardDashboardResponse overdueExpense();

    CardDashboardResponse realizedExpense();

    CardDashboardResponse pendingExpense();

    CardDashboardResponse grossExpense();

    List<CardDashboardResponse> balanceAccounts();

    List<CardDashboardResponse> invoices();

    ChartDoughnutResponse recipeChartCategory();

    ChartDoughnutResponse expenseChartCategory();

    void handleTransactionCacheEvent(TransactionCacheEvent event);
}
