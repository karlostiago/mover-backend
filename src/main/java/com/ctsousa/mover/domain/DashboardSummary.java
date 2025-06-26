package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.enumeration.Icon;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.response.CardDashboardResponse;
import com.ctsousa.mover.response.ChartDoughnutResponse;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        LocalDate today = LocalDate.now();

        var incomeTransactions = transactions.stream()
                .filter(t -> TypeCategory.INCOME.name().equalsIgnoreCase(t.getCategoryType()))
                .toList();

        var expenseTransactions = transactions.stream()
                .filter(this::isExpense)
                .toList();

        revenueCards.put("overdueRevenue", buildCard(
                incomeTransactions.stream()
                        .filter(t -> t.getDueDate().isBefore(today) && !t.getPaid())
                        .toList(),
                "Overdue Revenue"));

        revenueCards.put("realizedRevenue", buildCard(
                incomeTransactions.stream()
                        .filter(TransactionEntity::getPaid)
                        .toList(),
                "Realized Revenue"));

        revenueCards.put("pendingRevenue", buildCard(
                incomeTransactions.stream()
                        .filter(t -> !t.getDueDate().isBefore(today) && !t.getPaid())
                        .toList(),
                "Pending Revenue"));

        revenueCards.put("grossRevenue", buildCard(incomeTransactions, "Gross Revenue"));

        expenseCards.put("overdueExpense", buildCard(
                expenseTransactions.stream()
                        .filter(t -> t.getDueDate().isBefore(today) && !t.getPaid())
                        .toList(),
                "Overdue Expense"));

        expenseCards.put("realizedExpense", buildCard(
                expenseTransactions.stream()
                        .filter(TransactionEntity::getPaid)
                        .toList(),
                "Realized Expense"));

        expenseCards.put("pendingExpense", buildCard(
                expenseTransactions.stream()
                        .filter(t -> !t.getDueDate().isBefore(today) && !t.getPaid())
                        .toList(),
                "Pending Expense"));

        expenseCards.put("grossExpense", buildCard(expenseTransactions, "Gross Expense"));

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

        this.revenueChart = buildChart(incomeTransactions);
        this.expenseChart = buildChart(expenseTransactions);
    }

    public CardDashboardResponse getRevenueCard(String key) {
        return revenueCards.get(key);
    }

    public CardDashboardResponse getExpenseCard(String key) {
        return expenseCards.get(key);
    }

    private boolean isExpense(TransactionEntity entity) {
        return TypeCategory.EXPENSE.name().equalsIgnoreCase(entity.getCategoryType())
                || TypeCategory.INVESTMENT.name().equalsIgnoreCase(entity.getCategoryType());
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
