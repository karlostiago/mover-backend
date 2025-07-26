package com.ctsousa.mover.scheduler;

import com.ctsousa.mover.core.entity.CardEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GenerateMonthlyInvoiceScheduler implements Scheduler {

    protected final TransactionRepository repository;
    private final InvoiceService invoiceService;

    public GenerateMonthlyInvoiceScheduler(TransactionRepository repository, InvoiceService invoiceService) {
        this.repository = repository;
        this.invoiceService = invoiceService;
    }

    @Override
//    @Scheduled(cron = "0 5 1 * * *") // Executa uma vez por mês no dia 1 as 05h00 da manhã
    public void process() {
//        List<Long> ids = repository.findByPeriod(LocalDate.of(2024, 10, 1), LocalDate.of(2024, 12, 31));
//        List<TransactionEntity> entities = repository.findByIdInWithDetails(ids);
//        Map<Map.Entry<LocalDate, CardEntity>, List<TransactionEntity>> grouped = groupByDueDateAndCard(getCardTransactionOnly(entities));
//
//        for (Map.Entry<Map.Entry<LocalDate, CardEntity>, List<TransactionEntity>> entry : grouped.entrySet()) {
//            List<TransactionEntity> items = entry.getValue();
//            TransactionEntity invoice = invoiceService.toGenerateWithoutSendNotification(items.get(0));
//            for (TransactionEntity item : items) {
//                 item.setInvoiceId(invoice.getId());
//                 invoiceService.update(invoice, item, false);
//            }
//        }
    }

    private Map<Map.Entry<LocalDate, CardEntity>, List<TransactionEntity>> groupByDueDateAndCard(List<TransactionEntity> entities) {
        return entities.stream().collect(Collectors.groupingBy(
                    t -> Map.entry(t.getDueDate(), t.getCard())
                ));
    }

    private List<TransactionEntity> getCardTransactionOnly(List<TransactionEntity> entities) {
        return Optional.of(entities)
                .orElse(Collections.emptyList())
                .stream()
                .filter(t -> t.getCard() != null && !t.getInvoice())
                .toList();
    }
}
