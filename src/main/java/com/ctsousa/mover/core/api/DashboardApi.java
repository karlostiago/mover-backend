package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.CardDashboardResponse;
import com.ctsousa.mover.response.ChartDoughnutResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface DashboardApi {

    @GetMapping("/refresh")
    ResponseEntity<Void> refresh();

    @GetMapping("/contracts-active")
    ResponseEntity<CardDashboardResponse> activeContracts();

    @GetMapping("/terminated-contracts")
    ResponseEntity<CardDashboardResponse> terminatedContracts();

    @GetMapping("/rental-vehicles")
    ResponseEntity<CardDashboardResponse> rentalVehicles();

    @GetMapping("/stopped-vehicles")
    ResponseEntity<CardDashboardResponse> stoppedVehicles();

    @GetMapping("/overdue-revenue")
    ResponseEntity<CardDashboardResponse> overdueRevenue();

    @GetMapping("/realized-revenue")
    ResponseEntity<CardDashboardResponse> realizedRevenue();

    @GetMapping("/pending-revenue")
    ResponseEntity<CardDashboardResponse> pendingRevenue();

    @GetMapping("/gross-revenue")
    ResponseEntity<CardDashboardResponse> grossRevenue();

    @GetMapping("/overdue-expense")
    ResponseEntity<CardDashboardResponse> overdueExpense();

    @GetMapping("/realized-expense")
    ResponseEntity<CardDashboardResponse> realizedExpense();

    @GetMapping("/pending-expense")
    ResponseEntity<CardDashboardResponse> pendingExpense();

    @GetMapping("/gross-expense")
    ResponseEntity<CardDashboardResponse> grossExpense();

    @GetMapping("/balance-accounts")
    ResponseEntity<List<CardDashboardResponse>> balanceAccounts();

    @GetMapping("/invoices")
    ResponseEntity<List<CardDashboardResponse>> invoices();

    @GetMapping("/recipe-chart-category")
    ResponseEntity<ChartDoughnutResponse> recipeChartCategory();

    @GetMapping("/expense-chart-category")
    ResponseEntity<ChartDoughnutResponse> expenseChartCategory();
}
