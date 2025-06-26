package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.DashboardApi;
import com.ctsousa.mover.core.event.TransactionCacheEvent;
import com.ctsousa.mover.response.CardDashboardResponse;
import com.ctsousa.mover.response.ChartDoughnutResponse;
import com.ctsousa.mover.service.DashboardService;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardResource  implements DashboardApi {

    private final DashboardService dashboardService;

    public DashboardResource(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Override
    public ResponseEntity<Void> refresh() {
        handleTransactionCacheEvent(new TransactionCacheEvent());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<CardDashboardResponse> activeContracts() {
        return ResponseEntity.ok(dashboardService.activeContracts());
    }

    @Override
    public ResponseEntity<CardDashboardResponse> terminatedContracts() {
        return ResponseEntity.ok(dashboardService.terminatedContracts());
    }

    @Override
    public ResponseEntity<CardDashboardResponse> rentalVehicles() {
        return ResponseEntity.ok(dashboardService.rentalVehicles());
    }

    @Override
    public ResponseEntity<CardDashboardResponse> stoppedVehicles() {
        return ResponseEntity.ok(dashboardService.stoppedVehicles());
    }

    @Override
    public ResponseEntity<CardDashboardResponse> overdueRevenue() {
        return ResponseEntity.ok(dashboardService.overdueRevenue());
    }

    @Override
    public ResponseEntity<CardDashboardResponse> realizedRevenue() {
        return ResponseEntity.ok(dashboardService.realizedRevenue());
    }

    @Override
    public ResponseEntity<CardDashboardResponse> pendingRevenue() {
        return ResponseEntity.ok(dashboardService.pendingRevenue());
    }

    @Override
    public ResponseEntity<CardDashboardResponse> grossRevenue() {
        return ResponseEntity.ok(dashboardService.grossRevenue());
    }

    @Override
    public ResponseEntity<CardDashboardResponse> overdueExpense() {
        return ResponseEntity.ok(dashboardService.overdueExpense());
    }

    @Override
    public ResponseEntity<CardDashboardResponse> realizedExpense() {
        return ResponseEntity.ok(dashboardService.realizedExpense());
    }

    @Override
    public ResponseEntity<CardDashboardResponse> pendingExpense() {
        return ResponseEntity.ok(dashboardService.pendingExpense());
    }

    @Override
    public ResponseEntity<CardDashboardResponse> grossExpense() {
        return ResponseEntity.ok(dashboardService.grossExpense());
    }

    @Override
    public ResponseEntity<List<CardDashboardResponse>> balanceAccounts() {
        return ResponseEntity.ok(dashboardService.balanceAccounts());
    }

    @Override
    public ResponseEntity<List<CardDashboardResponse>> invoices() {
        return ResponseEntity.ok(dashboardService.invoices());
    }

    @Override
    public ResponseEntity<ChartDoughnutResponse> recipeChartCategory() {
        return ResponseEntity.ok(dashboardService.recipeChartCategory());
    }

    @Override
    public ResponseEntity<ChartDoughnutResponse> expenseChartCategory() {
        return ResponseEntity.ok(dashboardService.expenseChartCategory());
    }

    @EventListener
    public void handleTransactionCacheEvent(TransactionCacheEvent event) {
        dashboardService.handleTransactionCacheEvent(event);
    }
}
