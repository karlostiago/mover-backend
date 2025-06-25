package com.ctsousa.mover.scheduler;

import com.ctsousa.mover.core.entity.CardEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.repository.InvoiceRepository;
import com.ctsousa.mover.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GenerateMonthlyInvoiceScheduler implements Scheduler {

    protected final TransactionRepository repository;
    private final InvoiceRepository invoiceRepository;

    public GenerateMonthlyInvoiceScheduler(TransactionRepository repository, InvoiceRepository invoiceRepository) {
        this.repository = repository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
//    @Scheduled(cron = "0 5 1 * * *") // Executa uma vez por mês no dia 1 as 05h00 da manhã
//    @Scheduled(fixedDelay = 10000L)
    public void process() {
//        LocalDate dtInitial = LocalDate.now();
//        LocalDate dtFim = dtInitial.withDayOfMonth(dtInitial.lengthOfMonth());
//
//        log.info("Inciando geração de fatura....");
//
//        List<Long> page = repository.findByPeriod(dtInitial, dtFim);
//
//        List<TransactionEntity> entities = getCardTransactionOnly(repository.findByIdInWithDetails(page));
//
//        if (entities.isEmpty()) return;
//
//        Map<Map.Entry<LocalDate, CardEntity>, List<TransactionEntity>> groupedEntities = groupByDueDateAndCard(entities);
//
//        List<TransactionEntity> invoicesProcess = new ArrayList<>();
//
//        for (Map.Entry<Map.Entry<LocalDate, CardEntity>, List<TransactionEntity>> entry : groupedEntities.entrySet()) {
//            LocalDate dueDate = entry.getKey().getKey();
//            CardEntity card = entry.getKey().getValue();
//
//            log.info("Gerando fatura do cartao :: {}, com vencimento para :: {}", card.getName(), dueDate);
//            if (hasNotInvoice(dueDate, card)) {
//                invoicesProcess.addAll(entry.getValue());
//            }
//        }
//
//        log.info("Quantidade de faturas a serem geradas :: {}", invoicesProcess.size());
//
//        if (!invoicesProcess.isEmpty()) {
//            TransactionScheduler.add(invoicesProcess);
//        }
//
//        log.info("Finalizado geração de fatura.");
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

    private boolean hasInvoice(LocalDate dueDate, CardEntity card) {
        return invoiceRepository.findBy(dueDate, card) != null;
    }

    private boolean hasNotInvoice(LocalDate dueDate, CardEntity card) {
        return !hasInvoice(dueDate, card);
    }
}
