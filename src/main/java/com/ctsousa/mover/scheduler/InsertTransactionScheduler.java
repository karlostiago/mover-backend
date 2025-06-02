package com.ctsousa.mover.scheduler;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Component
public class InsertTransactionScheduler implements Scheduler {

    private static final Queue<List<TransactionEntity>> queue = new ConcurrentLinkedQueue<>();

    protected final TransactionRepository repository;
    protected final InvoiceService invoiceService;

    public InsertTransactionScheduler(TransactionRepository repository, InvoiceService invoiceService) {
        this.repository = repository;
        this.invoiceService = invoiceService;
    }

    @Override
    @Scheduled(cron = "0/1 * * * * *")
    public void process() {

        if (queue.isEmpty()) return;

        log.info("Iniciado processamento de insert de lançamentos :: {} ", LocalDateTime.now());
        while (!queue.isEmpty()) {
            List<TransactionEntity> entities = queue.poll();
            for (TransactionEntity entity : entities) {
                if (entity.getCard() != null) {
                    TransactionEntity invoice = invoiceService.toGenerate(entity);
                    entity.setInvoiceId(invoice.getId());
                }
                repository.save(entity);
            }
        }
        log.info("Finalizado processamento de insert de lançamentos :: {} ", LocalDateTime.now());
    }

    public static void add(final List<TransactionEntity> entities) {
        queue.add(entities);
    }
}
