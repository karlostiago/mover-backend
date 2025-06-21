package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.entity.ContractEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.core.event.TransactionCacheEvent;
import com.ctsousa.mover.core.util.DateUtil;
import com.ctsousa.mover.enumeration.Situation;
import com.ctsousa.mover.repository.ContractRepository;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.repository.VehicleRepository;
import com.ctsousa.mover.response.CardDashboardResponse;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.ctsousa.mover.core.util.StringUtil.normalizer;

@RestController
@RequestMapping("/dashboard")
public class DashboardResource  {

    private final ContractRepository contractRepository;
    private final VehicleRepository vehicleRepository;
    private final TransactionRepository transactionRepository;

    private List<TransactionEntity> cached;

    public DashboardResource(ContractRepository contractRepository, VehicleRepository vehicleRepository, TransactionRepository transactionRepository) {
        this.contractRepository = contractRepository;
        this.vehicleRepository = vehicleRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/contracts-active")
    public ResponseEntity<CardDashboardResponse> activeContracts() {
        List<ContractEntity> entities = contractRepository.findBy(Situation.ONGOING);
        CardDashboardResponse response = new CardDashboardResponse();
        response.setQuantity(entities.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/terminated-contracts")
    public ResponseEntity<CardDashboardResponse> terminatedContracts() {
        List<ContractEntity> entities = contractRepository.findBy(Situation.CLOSED);
        CardDashboardResponse response = new CardDashboardResponse();
        response.setQuantity(entities.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rental-vehicles")
    public ResponseEntity<CardDashboardResponse> rentalVehicles() {
        List<VehicleEntity> allEntities = vehicleRepository.findAll();
        List<VehicleEntity> avaliabbleEntities = vehicleRepository.onlyAvailable();
        CardDashboardResponse response = new CardDashboardResponse();
        response.setQuantity(allEntities.size() - avaliabbleEntities.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stopped-vehicles")
    public ResponseEntity<CardDashboardResponse> stoppedVehicles() {
        List<VehicleEntity> entities = vehicleRepository.onlyAvailable();
        CardDashboardResponse response = new CardDashboardResponse();
        response.setQuantity(entities.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/overdue-charges")
    public ResponseEntity<CardDashboardResponse> overdueCharges() {
        loadCached();

        LocalDate today = LocalDate.now();

        List<TransactionEntity> entities = cached.stream()
                .filter(t -> t.getDueDate().isBefore(today) && !t.getPaid())
                .filter(t -> "ALUGUEL".equalsIgnoreCase(t.getSubcategory().getDescription()))
                .toList();

        BigDecimal value = entities.stream()
                .map(TransactionEntity::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CardDashboardResponse response = new CardDashboardResponse();
        response.setQuantity(entities.size());
        response.setValue(value);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/charges-made")
    public ResponseEntity<CardDashboardResponse> chargesMade() {
        loadCached();

        List<TransactionEntity> entities = cached.stream()
                .filter(TransactionEntity::getPaid)
                .filter(t -> "ALUGUEL".equalsIgnoreCase(t.getSubcategory().getDescription()))
                .toList();

        BigDecimal value = entities.stream()
                .map(TransactionEntity::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CardDashboardResponse response = new CardDashboardResponse();
        response.setQuantity(entities.size());
        response.setValue(value);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/maintenance-performed")
    public ResponseEntity<CardDashboardResponse> maintenancePerformed() {
        loadCached();

        List<TransactionEntity> entities = cached.stream()
                .filter(t -> normalizer("MANUTENÇÃO")
                        .equalsIgnoreCase(normalizer(t.getSubcategory().getDescription())))
                .toList();

        BigDecimal value = entities.stream()
                .map(TransactionEntity::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .abs();

        CardDashboardResponse response = new CardDashboardResponse();
        response.setQuantity(entities.size());
        response.setValue(value);
        return ResponseEntity.ok(response);
    }

    @EventListener
    public void hendleTransactionCacheEvent(TransactionCacheEvent event) {
        this.cached = null;
    }

    private void loadCached() {
        if (cached == null) {
            LocalDate dtInicial = DateUtil.getFirstDay(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());
            LocalDate dtFinal = DateUtil.getLastDay(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());
            cached = transactionRepository
                        .findByPeriod(dtInicial, dtFinal, PageRequest.of(0, Integer.MAX_VALUE))
                        .getContent();
        }
    }
}
