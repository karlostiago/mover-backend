package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.entity.*;
import com.ctsousa.mover.core.event.TransactionCacheEvent;
import com.ctsousa.mover.core.util.DateUtil;
import com.ctsousa.mover.enumeration.Icon;
import com.ctsousa.mover.enumeration.Situation;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.repository.ContractRepository;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.repository.VehicleRepository;
import com.ctsousa.mover.response.CardDashboardResponse;
import com.ctsousa.mover.response.ChartDoughnutResponse;
import com.ctsousa.mover.service.AccountService;
import com.ctsousa.mover.service.CardService;
import com.ctsousa.mover.service.InvoiceService;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ctsousa.mover.core.util.StringUtil.normalizer;

@RestController
@RequestMapping("/dashboard")
public class DashboardResource  {

    private final ContractRepository contractRepository;
    private final VehicleRepository vehicleRepository;
    private final TransactionRepository transactionRepository;
    private final InvoiceService invoiceService;
    private final AccountService accountService;
    private final CardService cardService;

    private List<TransactionEntity> cached;
    private List<AccountEntity> accounts;
    private List<CardEntity> cards;

    public DashboardResource(ContractRepository contractRepository, VehicleRepository vehicleRepository, TransactionRepository transactionRepository, InvoiceService invoiceService, AccountService accountService, CardService cardService) {
        this.contractRepository = contractRepository;
        this.vehicleRepository = vehicleRepository;
        this.transactionRepository = transactionRepository;
        this.invoiceService = invoiceService;
        this.accountService = accountService;
        this.cardService = cardService;
    }

    @GetMapping("/contracts-active")
    public ResponseEntity<CardDashboardResponse> activeContracts() {
        List<ContractEntity> entities = contractRepository.findBy(Situation.ONGOING);
        CardDashboardResponse response = new CardDashboardResponse();
        response.setLoading(true);
        response.setQuantity(entities.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/terminated-contracts")
    public ResponseEntity<CardDashboardResponse> terminatedContracts() {
        List<ContractEntity> entities = contractRepository.findBy(Situation.CLOSED);
        CardDashboardResponse response = new CardDashboardResponse();
        response.setLoading(true);
        response.setQuantity(entities.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rental-vehicles")
    public ResponseEntity<CardDashboardResponse> rentalVehicles() {
        List<VehicleEntity> allEntities = vehicleRepository.findAll();
        List<VehicleEntity> avaliabbleEntities = vehicleRepository.onlyAvailable();
        CardDashboardResponse response = new CardDashboardResponse();
        response.setLoading(true);
        response.setQuantity(allEntities.size() - avaliabbleEntities.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stopped-vehicles")
    public ResponseEntity<CardDashboardResponse> stoppedVehicles() {
        List<VehicleEntity> entities = vehicleRepository.onlyAvailable();
        CardDashboardResponse response = new CardDashboardResponse();
        response.setLoading(true);
        response.setQuantity(entities.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/overdue-revenue")
    public ResponseEntity<CardDashboardResponse> overdueRevenue() {
        loadCached();

        LocalDate today = LocalDate.now();
        List<TransactionEntity> entities = cached.stream()
                .filter(t -> t.getDueDate().isBefore(today) && !t.getPaid())
                .filter(t -> TypeCategory.INCOME.name().equalsIgnoreCase(t.getCategoryType()))
                .toList();

        BigDecimal value = entities.stream()
                .map(TransactionEntity::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CardDashboardResponse response = new CardDashboardResponse();
        response.setQuantity(entities.size());
        response.setValue(value);
        response.setLoading(true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/realized-revenue")
    public ResponseEntity<CardDashboardResponse> realizedRevenue() {
        loadCached();

        List<TransactionEntity> entities = cached.stream()
                .filter(TransactionEntity::getPaid)
                .filter(t -> TypeCategory.INCOME.name().equalsIgnoreCase(t.getCategoryType()))
                .toList();

        BigDecimal value = entities.stream()
                .map(TransactionEntity::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CardDashboardResponse response = new CardDashboardResponse();
        response.setQuantity(entities.size());
        response.setValue(value);
        response.setLoading(true);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/pending-revenue")
    public ResponseEntity<CardDashboardResponse> pendingRevenue() {
        loadCached();

        LocalDate today = LocalDate.now();
        List<TransactionEntity> entities = cached.stream()
                .filter(t -> !t.getDueDate().isBefore(today) && !t.getPaid())
                .filter(t -> TypeCategory.INCOME.name().equalsIgnoreCase(t.getCategoryType()))
                .toList();

        BigDecimal value = entities.stream()
                .map(TransactionEntity::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CardDashboardResponse response = new CardDashboardResponse();
        response.setQuantity(entities.size());
        response.setValue(value);
        response.setLoading(true);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/gross-revenue")
    public ResponseEntity<CardDashboardResponse> grossRevenue() {
        loadCached();

        List<TransactionEntity> entities = cached.stream()
                .filter(t -> TypeCategory.INCOME.name().equalsIgnoreCase(t.getCategoryType()))
                .toList();

        BigDecimal value = entities.stream()
                .map(TransactionEntity::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CardDashboardResponse response = new CardDashboardResponse();
        response.setQuantity(entities.size());
        response.setValue(value);
        response.setLoading(true);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/overdue-expense")
    public ResponseEntity<CardDashboardResponse> overdueExpense() {
        loadCached();

        LocalDate today = LocalDate.now();
        List<TransactionEntity> entities = cached.stream()
                .filter(t -> t.getDueDate().isBefore(today) && !t.getPaid())
                .filter(t -> TypeCategory.EXPENSE.name().equalsIgnoreCase(t.getCategoryType()))
                .toList();

        BigDecimal value = entities.stream()
                .map(TransactionEntity::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CardDashboardResponse response = new CardDashboardResponse();
        response.setQuantity(entities.size());
        response.setValue(value);
        response.setLoading(true);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/realized-expense")
    public ResponseEntity<CardDashboardResponse> realizedExpense() {
        loadCached();

        List<TransactionEntity> entities = cached.stream()
                .filter(TransactionEntity::getPaid)
                .filter(t -> TypeCategory.EXPENSE.name().equalsIgnoreCase(t.getCategoryType()))
                .toList();

        BigDecimal value = entities.stream()
                .map(TransactionEntity::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CardDashboardResponse response = new CardDashboardResponse();
        response.setQuantity(entities.size());
        response.setValue(value);
        response.setLoading(true);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/pending-expense")
    public ResponseEntity<CardDashboardResponse> pendingExpense() {
        loadCached();

        LocalDate today = LocalDate.now();
        List<TransactionEntity> entities = cached.stream()
                .filter(t -> !t.getDueDate().isBefore(today) && !t.getPaid())
                .filter(t -> TypeCategory.EXPENSE.name().equalsIgnoreCase(t.getCategoryType()))
                .toList();

        BigDecimal value = entities.stream()
                .map(TransactionEntity::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CardDashboardResponse response = new CardDashboardResponse();
        response.setQuantity(entities.size());
        response.setValue(value);
        response.setLoading(true);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/gross-expense")
    public ResponseEntity<CardDashboardResponse> grossExpense() {
        loadCached();

        List<TransactionEntity> entities = cached.stream()
                .filter(t -> TypeCategory.EXPENSE.name().equalsIgnoreCase(t.getCategoryType()))
                .toList();

        BigDecimal value = entities.stream()
                .map(TransactionEntity::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CardDashboardResponse response = new CardDashboardResponse();
        response.setQuantity(entities.size());
        response.setValue(value);
        response.setLoading(true);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/balance-accounts")
    public ResponseEntity<List<CardDashboardResponse>> balanceAccounts() {
        List<CardDashboardResponse> responses = new ArrayList<>();
        for (AccountEntity entity : accounts) {
            if (!entity.getActive()) continue;
            CardDashboardResponse response = new CardDashboardResponse();
            response.setDescription(entity.getName());
            response.setValue(entity.getAvailableBalance());
            response.setIconPath(Icon.toName(entity.getIcon()).getUrlImage());
            response.setLoading(true);
            responses.add(response);
        }
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/invoices")
    public ResponseEntity<List<CardDashboardResponse>> invoices() {
        List<CardDashboardResponse> responses = new ArrayList<>();
        for (CardEntity entity : cards) {
            if (!entity.getActive()) continue;
            LocalDate dtInicial = DateUtil.getFirstDay(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());
            LocalDate dtFinal = DateUtil.getLastDay(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());
            var value = cardService.calculateInvoiceValue(entity, dtInicial, dtFinal);
            CardDashboardResponse response = new CardDashboardResponse();
            response.setDescription(entity.getName());
            response.setValue(value);
            response.setLoading(true);
            response.setIconPath(Icon.toName(entity.getIcon()).getUrlImage());
            responses.add(response);
        }
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/recipe-chart-category")
    public ResponseEntity<ChartDoughnutResponse> recipeChartCategory() {
        LocalDate dtInicial = DateUtil.getFirstDay(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());
        LocalDate dtFinal = DateUtil.getLastDay(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());
        List<TransactionEntity> entities = transactionRepository.findBy(dtInicial, dtFinal, "INCOME");

        Map<String, BigDecimal> grouped = entities.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getSubcategory().getDescription(),
                        LinkedHashMap::new,
                        Collectors.reducing(BigDecimal.ZERO, TransactionEntity::getValue, BigDecimal::add)
                ));

        ChartDoughnutResponse response = new ChartDoughnutResponse();
        response.setLabels(new ArrayList<>(grouped.keySet()));
        response.setValues(new ArrayList<>(grouped.values()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/expense-chart-category")
    public ResponseEntity<ChartDoughnutResponse> expenseChartCategory() {
        LocalDate dtInicial = DateUtil.getFirstDay(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());
        LocalDate dtFinal = DateUtil.getLastDay(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());
        List<TransactionEntity> entities = transactionRepository.findBy(dtInicial, dtFinal, "EXPENSE");

        Map<String, BigDecimal> grouped = entities.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getSubcategory().getDescription(),
                        LinkedHashMap::new,
                        Collectors.reducing(BigDecimal.ZERO, TransactionEntity::getValue, BigDecimal::add)
                ));

        ChartDoughnutResponse response = new ChartDoughnutResponse();
        response.setLabels(new ArrayList<>(grouped.keySet()));
        response.setValues(new ArrayList<>(grouped.values()));
        return ResponseEntity.ok(response);
    }

    @EventListener
    public void hendleTransactionCacheEvent(TransactionCacheEvent event) {
        this.cached = null;
        this.accounts = null;
        this.cards = null;
    }

    private void loadCached() {
        if (cached == null) {
            LocalDate dtInicial = DateUtil.getFirstDay(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());
            LocalDate dtFinal = DateUtil.getLastDay(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());
            Page<Long> page = transactionRepository.findByPeriod(dtInicial, dtFinal, PageRequest.of(0, Integer.MAX_VALUE));
            cached = transactionRepository.findByIdInWithDetails(page.getContent());
            accounts = accountService.findAll();
            cards = cardService.findAll();
        }
    }
}
