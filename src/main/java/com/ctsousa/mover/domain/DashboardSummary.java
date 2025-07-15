package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.enumeration.DashboardType;
import com.ctsousa.mover.enumeration.Icon;
import com.ctsousa.mover.response.CardDashboardResponse;
import com.ctsousa.mover.response.ChartDoughnutResponse;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardSummary {

    @Getter
    private final List<CardDashboardResponse> accountBalances;

    @Getter
    private final List<CardDashboardResponse> invoices;

    @Getter
    private final ChartDoughnutResponse revenueChart;

    @Getter
    private final ChartDoughnutResponse expenseChart;

    private final Map<String, CardDashboardResponse> revenueCards = new HashMap<>();

    private final Map<String, CardDashboardResponse> expenseCards = new HashMap<>();

    private final Map<String, CardDashboardResponse> otherCards = new HashMap<>();

    public DashboardSummary(List<TransactionEntity> transactions,
                            List<AccountEntity> accounts,
                            List<CardDashboardResponse> invoices) {

        revenueCards.put("overdueRevenue", buildCard(from(transactions, DashboardType.OVERDUE_REVENUE), "Overdue Revenue"));
        revenueCards.put("realizedRevenue", buildCard(from(transactions, DashboardType.REALIZED_REVENUE), "Realized Revenue"));
        revenueCards.put("pendingRevenue", buildCard(from(transactions, DashboardType.PENDING_REVENUE), "Pending Revenue"));
        revenueCards.put("grossRevenue", buildCard(from(transactions, DashboardType.GROSS_REVENUE), "Gross Revenue"));
        expenseCards.put("overdueExpense", buildCard(from(transactions, DashboardType.OVERDUE_EXPENSE), "Overdue Expense"));
        expenseCards.put("realizedExpense", buildCard(from(transactions, DashboardType.REALIZED_EXPENSE), "Realized Expense"));
        expenseCards.put("pendingExpense", buildCard(from(transactions, DashboardType.PENDING_EXPENSE), "Pending Expense"));
        expenseCards.put("grossExpense", buildCard(from(transactions, DashboardType.GROSS_EXPENSE), "Gross Expense"));

        this.accountBalances = accounts.stream()
                .filter(AccountEntity::getActive)
                .map(account -> CardDashboardResponse.builder()
                        .value(account.getAvailableBalance())
                        .description(account.getName())
                        .iconPath(Icon.toName(account.getIcon()).getUrlImage())
                        .loading(true)
                        .build())
                .toList();

        this.invoices = invoices;

        this.revenueChart = buildChart(from(transactions, DashboardType.GROSS_REVENUE));
        this.expenseChart = buildChart(from(transactions, DashboardType.GROSS_EXPENSE));
    }

    public static List<TransactionEntity> from(List<TransactionEntity> entities, DashboardType dashboardType) {
       return DashboardType.from(dashboardType.name())
                .map(type -> type.filter(entities))
                .orElse(Collections.emptyList());
    }

    public CardDashboardResponse getRevenueCard(String key) {
        return revenueCards.get(key);
    }

    public CardDashboardResponse getExpenseCard(String key) {
        return expenseCards.get(key);
    }

    private CardDashboardResponse buildCard(List<TransactionEntity> transactions, String description) {
        BigDecimal totalValue = transactions.stream()
                .map(TransactionEntity::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CardDashboardResponse.builder()
                .description(description)
                .loading(true)
                .value(totalValue)
                .quantity(transactions.size())
                .build();
    }

    private ChartDoughnutResponse buildChart(List<TransactionEntity> transactions) {
        var grouped = transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getSubcategory().getDescription(),
                        LinkedHashMap::new,
                        Collectors.reducing(BigDecimal.ZERO, TransactionEntity::getValue, BigDecimal::add)
                ));

        ChartDoughnutResponse response = new ChartDoughnutResponse();
        response.setLabels(new ArrayList<>(grouped.keySet()));
        response.setValues(new ArrayList<>(grouped.values()));
        return response;
    }

    public void addOtherCard(String key, CardDashboardResponse response) {
        otherCards.put(key, response);
    }

    public CardDashboardResponse getOtherCard(String key) {
        return this.otherCards.get(key);
    }
}
