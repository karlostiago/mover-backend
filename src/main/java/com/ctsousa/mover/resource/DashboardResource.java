package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.event.TransactionCacheEvent;
import com.ctsousa.mover.response.CardDashboardResponse;
import com.ctsousa.mover.response.ChartDoughnutResponse;
import com.ctsousa.mover.service.DashboardService;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardResource  {

    private final DashboardService dashboardService;

    public DashboardResource(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/contracts-active")
    public ResponseEntity<CardDashboardResponse> activeContracts() {
        return ResponseEntity.ok(dashboardService.activeContracts());
    }

    @GetMapping("/terminated-contracts")
    public ResponseEntity<CardDashboardResponse> terminatedContracts() {
        return ResponseEntity.ok(dashboardService.terminatedContracts());
    }

    @GetMapping("/rental-vehicles")
    public ResponseEntity<CardDashboardResponse> rentalVehicles() {
        return ResponseEntity.ok(dashboardService.rentalVehicles());
    }

    @GetMapping("/stopped-vehicles")
    public ResponseEntity<CardDashboardResponse> stoppedVehicles() {
        return ResponseEntity.ok(dashboardService.stoppedVehicles());
    }

    @GetMapping("/overdue-revenue")
    public ResponseEntity<CardDashboardResponse> overdueRevenue() {
        return ResponseEntity.ok(dashboardService.overdueRevenue());
    }

    @GetMapping("/realized-revenue")
    public ResponseEntity<CardDashboardResponse> realizedRevenue() {
        return ResponseEntity.ok(dashboardService.realizedRevenue());
    }

    @GetMapping("/pending-revenue")
    public ResponseEntity<CardDashboardResponse> pendingRevenue() {
        return ResponseEntity.ok(dashboardService.pendingRevenue());
    }

    @GetMapping("/gross-revenue")
    public ResponseEntity<CardDashboardResponse> grossRevenue() {
        return ResponseEntity.ok(dashboardService.grossRevenue());
    }

    @GetMapping("/overdue-expense")
    public ResponseEntity<CardDashboardResponse> overdueExpense() {
        return ResponseEntity.ok(dashboardService.overdueExpense());
    }

    @GetMapping("/realized-expense")
    public ResponseEntity<CardDashboardResponse> realizedExpense() {
        return ResponseEntity.ok(dashboardService.realizedExpense());
    }

    @GetMapping("/pending-expense")
    public ResponseEntity<CardDashboardResponse> pendingExpense() {
        return ResponseEntity.ok(dashboardService.pendingExpense());
    }

    @GetMapping("/gross-expense")
    public ResponseEntity<CardDashboardResponse> grossExpense() {
        return ResponseEntity.ok(dashboardService.grossExpense());
    }

    @GetMapping("/balance-accounts")
    public ResponseEntity<List<CardDashboardResponse>> balanceAccounts() {
        return ResponseEntity.ok(dashboardService.balanceAccounts());
    }

    @GetMapping("/invoices")
    public ResponseEntity<List<CardDashboardResponse>> invoices() {
        return ResponseEntity.ok(dashboardService.invoices());
    }

    @GetMapping("/recipe-chart-category")
    public ResponseEntity<ChartDoughnutResponse> recipeChartCategory() {
        return ResponseEntity.ok(dashboardService.recipeChartCategory());
    }

    @GetMapping("/expense-chart-category")
    public ResponseEntity<ChartDoughnutResponse> expenseChartCategory() {
        return ResponseEntity.ok(dashboardService.expenseChartCategory());
    }

    @EventListener
    public void hendleTransactionCacheEvent(TransactionCacheEvent event) {
        dashboardService.handleTransactionCacheEvent(event);
    }
}
