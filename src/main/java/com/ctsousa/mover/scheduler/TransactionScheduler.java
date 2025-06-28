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
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class TransactionScheduler implements Scheduler {

    private static final Queue<List<TransactionEntity>> queue = new ConcurrentLinkedQueue<>();
    private static final Queue<List<TransactionEntity>> pedingQueue = new ConcurrentLinkedQueue<>();
    private static final AtomicBoolean processing = new AtomicBoolean(false);

    protected final TransactionRepository repository;
    protected final InvoiceService invoiceService;

    public TransactionScheduler(TransactionRepository repository, InvoiceService invoiceService) {
        this.repository = repository;
        this.invoiceService = invoiceService;
    }

    @Override
    @Scheduled(cron = "0/1 * * * * *")
    public void process() {
        if (!processing.compareAndSet(false, true)) return;

        try {
            if (queue.isEmpty()) return;

            log.info("Iniciado processamento de insert de lançamentos :: {} ", LocalDateTime.now());
            while (!queue.isEmpty()) {
                List<TransactionEntity> entities = queue.poll();
                for (TransactionEntity entity : entities) {
                    if (entity.getInvoiceId() != null) {
                        TransactionEntity invoice = invoiceService.findById(entity.getInvoiceId());
                        invoiceService.update(invoice, entity, true);
                        continue;
                    }
                    if (entity.getCard() != null) {
                        TransactionEntity invoice = invoiceService.toGenerateSendNotification(entity);
                        entity.setInvoiceId(invoice.getId());
                    }
                    repository.save(entity);
                }
            }

            while (!pedingQueue.isEmpty()) {
                queue.add(pedingQueue.poll());
            }

            log.info("Finalizado processamento de insert de lançamentos :: {} ", LocalDateTime.now());


        } catch(Exception e) {
            log.error("Erro ao processar transações :: ", e);
        } finally {
            processing.set(false);
        }
    }

    public static synchronized void add(final List<TransactionEntity> entities) {
        if (processing.get()) {
            log.info("Processamento em andamento. Adicionando novo lote a fila de espera...");
            pedingQueue.add(entities);
        } else {
            queue.add(entities);
        }
    }

    public static void add(final TransactionEntity entity) {
        add(List.of(entity));
    }
}
