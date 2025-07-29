package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.CardEntity;
import com.ctsousa.mover.core.entity.InvoicePaymentDetailEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.repository.InvoicePaymentDetailRepository;
import com.ctsousa.mover.repository.InvoiceRepository;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.CardService;
import com.ctsousa.mover.service.InvoicePaymentService;
import com.ctsousa.mover.service.InvoiceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ctsousa.mover.core.util.DateUtil.monthInFull;
import static com.ctsousa.mover.core.util.StringUtil.removeDuplicateWords;
import static java.util.UUID.randomUUID;

@Component
public class InvoiceServiceImpl extends BaseServiceImpl<TransactionEntity, Long> implements InvoiceService {

    private static final String name = "FATURA CARTÃO %s %s %d";

    @Autowired
    private InvoiceRepository repository;
    private final CardService cardService;
    private final InvoicePaymentService invoicePaymentService;
    private final InvoicePaymentDetailRepository invoicePaymentDetailRepository;

    public InvoiceServiceImpl(TransactionRepository repository, CardService cardService, InvoicePaymentService invoicePaymentService, InvoicePaymentDetailRepository invoicePaymentDetailRepository) {
        super(repository);
        this.cardService = cardService;
        this.invoicePaymentService = invoicePaymentService;
        this.invoicePaymentDetailRepository = invoicePaymentDetailRepository;
    }

    @Override
    public void updateBalance(LocalDate dueDate, CardEntity card) {
        TransactionEntity invoice = repository.findBy(dueDate, card);
        if (invoice == null) {
            throw new NotificationException("Fatura não encontrada para atualizar o saldo.");
        }

        List<TransactionEntity> invoiceItems = getInvoiceItems(invoice);
        BigDecimal value = recalculate(invoiceItems);

        invoice.setValue(value);
        updateTransactionTypeAndTypeCategoryWhenCredit(value, invoice);
        repository.save(invoice);
    }

    @Override
    public TransactionEntity save(TransactionEntity entity) {
        CardEntity card = cardService.findById(entity.getCard().getId());
        LocalDate dueDate = entity.getDueDate();
        var invoice = repository.save(create(entity, createDescription(card, dueDate)));
        entity.setInvoiceId(invoice.getId());
        repository.save(entity);
        return invoice;
    }

    @Override
    public TransactionEntity update(TransactionEntity entity) {
        TransactionEntity existingInvoice = repository.findBy(entity.getDueDate(), entity.getCard());

        if (existingInvoice == null) {
            return save(entity);
        }

        entity.setInvoiceId(existingInvoice.getId());

        List<TransactionEntity> invoiceItems = getInvoiceItems(existingInvoice);
        invoiceItems.add(entity);

        BigDecimal value = recalculate(invoiceItems);

        existingInvoice.setValue(value);
        updateTransactionTypeAndTypeCategoryWhenCredit(value, existingInvoice);
        repository.saveAll(List.of(existingInvoice, entity));

        return entity;
    }

    @Override
    public TransactionEntity updateItem(Long id, TransactionEntity invoiceItem, boolean sendNotify) {
        TransactionEntity existingInvoice = repository.findById(invoiceItem.getInvoiceId())
                .orElseThrow(() -> new NotificationException("Fatura não encontrada"));

        TransactionEntity invoiceItemExists = findById(id);

        if (isDueDateOrCardChanged(invoiceItem, invoiceItemExists)) {
            return changeInvoiceItem(invoiceItemExists, invoiceItem);
        }

        BeanUtils.copyProperties(invoiceItem, invoiceItemExists, "id", "signature");

        if (Objects.isNull(invoiceItemExists.getCard())) {
            invoiceItemExists.setInvoiceId(null);
            invoiceItem.setInvoiceId(existingInvoice.getInvoiceId());
        }

        List<TransactionEntity> invoiceItems = getInvoiceItems(existingInvoice);
        invoiceItems.remove(invoiceItem);

        if (Objects.nonNull(invoiceItemExists.getCard())) {
            invoiceItems.add(invoiceItemExists);
        }

        BigDecimal value = recalculate(invoiceItems);
        existingInvoice.setValue(value);
        updateTransactionTypeAndTypeCategoryWhenCredit(value, existingInvoice);
        repository.saveAll(List.of(existingInvoice, invoiceItemExists));

        return invoiceItemExists;
    }

    @Override
    public void deleteById(Long id) {
        TransactionEntity entity = findById(id);
        if (entity.getInvoice()) {
            delete(entity, id);
        } else {
            deleteItem(entity);
        }
    }

    @Override
    public void delete(TransactionEntity entity) {
        if (entity.getInvoice()) {
            delete(entity, entity.getId());
        } else {
            deleteItem(entity);
        }
    }

    @Override
    public List<TransactionEntity> searchById(Long id) {
        id = invoicePaymentService.findPaymentId(id);
        List<TransactionEntity> entities = repository.findBy(id);
        return entities.stream()
                .sorted(Comparator.comparing(TransactionEntity::getRegisterDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public TransactionEntity schedule(Long id) {
        List<TransactionEntity> entities = getInvoice(id, true);
        entities.forEach(t -> repository.save(t));
        return entities.stream().filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotificationException("Não existe fatura para realizar agendamento."));
    }

    @Override
    public TransactionEntity undoScheduling(Long id) {
        List<TransactionEntity> entities = getInvoice(id, false);
        entities.forEach(t -> repository.save(t));
        return entities.stream().filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotificationException("Não existe fatura para desfazer agendamento."));
    }

    @Override
    public TransactionEntity pay(Transaction transaction) {
        List<TransactionEntity> entities = searchById(transaction.getId());

        entities.forEach(t -> {
            t.setRefund(false);
            t.setPaid(true);
            t.setPaymentDate(transaction.getPaymentDate());
        });

        TransactionEntity existingInvoice = getInvoice(entities, transaction.getId());

        entities.forEach(e -> repository.save(e));

        return invoicePaymentService.create(existingInvoice, existingInvoice.getAccount(),
                transaction.getPaymentDate(), transaction.getValue());
    }

    @Override
    public TransactionEntity refund(Long id) {
        List<TransactionEntity> entities = searchById(id);

        if (entities.isEmpty()) {
            return refundItem(id);
        }

        TransactionEntity existingInvoice = entities.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotificationException("Fatura não encontrada"));
        updateRefundAndPaidAndResidualValue(entities);

        entities.forEach(t -> repository.save(t));

        invoicePaymentService.findByPaymentDetails(existingInvoice.getId())
                .forEach(detail -> {
                    invoicePaymentService.deletePaymentDetail(detail.getId());
                    super.deleteById(detail.getPayment().getId());
                });

        return existingInvoice;
    }

    @Override
    public TransactionEntity next(Long cardId, LocalDate dueDate) {
        CardEntity entity = cardService.findById(cardId);
        TransactionEntity invoice = null;

        for (int month = 1; month <= 12; month++) {
            LocalDate nextDueDate = dueDate.plusMonths(month);
            invoice = repository.findBy(nextDueDate, entity);
            if (invoice != null) break;
        }

        if (invoice == null) throw new NotificationException("Não há mais faturas.", Severity.INFO);

        return invoice;
    }

    @Override
    public TransactionEntity previous(Long cardId, LocalDate dueDate) {
        CardEntity entity = cardService.findById(cardId);
        TransactionEntity invoice = null;

        for (int month = 1; month <= 12; month++) {
            LocalDate nextDueDate = dueDate.minusMonths(month);
            invoice = repository.findBy(nextDueDate, entity);
            if (invoice != null) break;
        }

        if (invoice == null) throw new NotificationException("Não há mais faturas.", Severity.INFO);

        return invoice;
    }

    @Override
    public Boolean exists(LocalDate dueDate, CardEntity card) {
        TransactionEntity invoice = repository.findBy(dueDate, card);
        return invoice != null;
    }

    private Boolean isDueDateOrCardChanged(TransactionEntity invoiceItem, TransactionEntity existingItem) {
        Boolean differentDueDate = !invoiceItem.getDueDate().isEqual(existingItem.getDueDate());
        Boolean differentCard = Objects.nonNull(invoiceItem.getCard()) && Objects.nonNull(existingItem.getCard())
                && !invoiceItem.getCard().getId().equals(existingItem.getCard().getId());
        return differentCard || differentDueDate;
    }

    private TransactionEntity getInvoice(List<TransactionEntity> entities, Long id) {
        return entities.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotificationException("Fatura não encontrada"));
    }

    private TransactionEntity refundItem(Long id) {
        InvoicePaymentDetailEntity paymentDetail = invoicePaymentDetailRepository.findById(id)
                .orElseThrow(() -> new NotificationException("Detalhe de pagamento não encontrado"));

        List<InvoicePaymentDetailEntity> details = invoicePaymentDetailRepository.findByInvoiceId(paymentDetail.getInvoice().getId());
        List<TransactionEntity> entities = searchById(paymentDetail.getInvoice().getId());

        details.removeIf(detail -> detail.getId().equals(paymentDetail.getId()));

        invoicePaymentDetailRepository.delete(paymentDetail);
        invoicePaymentService.deleteById(paymentDetail.getPayment().getId());

        if (details.isEmpty()) {
            updateRefundAndPaidAndResidualValue(entities);
            entities.forEach(t -> repository.save(t));
        }

        return paymentDetail.getInvoice();
    }

    private TransactionEntity changeInvoiceItem(TransactionEntity previousInvoiceItem, TransactionEntity currentInvoiceItem) {
        TransactionEntity previousInvoice = repository.findById(previousInvoiceItem.getInvoiceId())
                .orElseThrow(() -> new NotificationException("Fatura anterior não encontrada"));

        List<TransactionEntity> previousItems = getInvoiceItems(previousInvoice);
        previousInvoiceItem.setInvoiceId(null);
        previousItems.remove(previousInvoiceItem);

        updateInvoiceAndInvoiceItem(previousInvoice, previousInvoiceItem, previousItems);

        TransactionEntity currentInvoice = repository.findBy(currentInvoiceItem.getDueDate(), currentInvoiceItem.getCard());
        boolean isCreated = false;

        if (currentInvoice == null) {
            currentInvoiceItem.setSignature(previousInvoice.getSignature());
            currentInvoice = save(currentInvoiceItem);
            isCreated = true;
        }

        BeanUtils.copyProperties(previousInvoiceItem, currentInvoiceItem, "id", "dueDate");
        List<TransactionEntity> currentItems = getInvoiceItems(currentInvoice);
        currentInvoiceItem.setInvoiceId(currentInvoice.getId());

        if (!isCreated) {
            currentItems.add(currentInvoiceItem);
        }

        updateInvoiceAndInvoiceItem(currentInvoice, currentInvoiceItem, currentItems);

        return currentInvoiceItem;
    }

    private void updateInvoiceAndInvoiceItem(TransactionEntity invoice, TransactionEntity invoiceItem, List<TransactionEntity> invoiceItems) {
        BigDecimal value = recalculate(invoiceItems);
        invoice.setValue(value);
        updateTransactionTypeAndTypeCategoryWhenCredit(value, invoice);
        repository.saveAll(List.of(invoice, invoiceItem));

    }

    private BigDecimal recalculate(List<TransactionEntity> invoiceItems) {
        return invoiceItems.stream()
                .map(TransactionEntity::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<TransactionEntity> getInvoiceItems(TransactionEntity invoice) {
        return repository.findBy(invoice.getId())
                .stream()
                .filter(t -> !t.getInvoice())
                .collect(Collectors.toList());
    }

    private void updateRefundAndPaidAndResidualValue(List<TransactionEntity> entities) {
        entities.forEach(t -> {
            t.setRefund(true);
            t.setPaid(false);
            t.setPaymentDate(null);
            if (Boolean.TRUE.equals(t.getInvoice())) {
                t.setResidualValue(BigDecimal.ZERO);
            }
        });
    }

    private String createDescription(CardEntity card, LocalDate dueDate) {
        return removeDuplicateWords(String.format(name, card.getName(), monthInFull(dueDate), dueDate.getYear()));
    }

    private List<TransactionEntity> getInvoice(Long id, boolean schedule) {
        List<TransactionEntity> entities = searchById(id);
        entities.forEach(t -> t.setScheduled(schedule));
        return entities;
    }

    private TransactionEntity create(TransactionEntity entity, String description) {
        TransactionEntity invoice = new TransactionEntity();
        BeanUtils.copyProperties(entity, invoice);
        invoice.setId(null);
        invoice.setDescription(description);
        invoice.setVehicle(null);
        invoice.setContract(null);
        invoice.setSignature(String.valueOf(randomUUID()));
        invoice.setPartner(null);
        invoice.setPaid(false);
        invoice.setRefund(entity.getRefund());
        invoice.setHour(LocalTime.now());
        invoice.setLastInstallment(false);
        invoice.setScheduled(entity.getScheduled());
        invoice.setInvoiceId(null);
        invoice.setInvoice(true);
        invoice.setPredicted(false);
        invoice.setActive(true);
        invoice.setPaymentType("SINGLE");
        return invoice;
    }

    private void delete(TransactionEntity entity, Long id) {
        Long paymentId = invoicePaymentService.findPaymentId(entity.getId());
        List<InvoicePaymentDetailEntity> paymentDetails = invoicePaymentService.findByPaymentDetails(paymentId);
        InvoicePaymentDetailEntity invoicePaymentDetail = paymentDetails.stream().filter(d -> d.getPayment().getId().equals(id))
                .findFirst()
                .orElse(null);

        boolean hasPaymentDetails = invoicePaymentDetail != null;
        if (hasPaymentDetails) {
            paymentDetails.remove(invoicePaymentDetail);
            invoicePaymentService.deletePaymentDetail(invoicePaymentDetail.getId());
        }

        boolean haveInvoicePaidNoPaymentDetails = entity.getPaid() && paymentDetails.isEmpty();
        if (haveInvoicePaidNoPaymentDetails) {
            List<TransactionEntity> entities = searchById(paymentId);
            entities.forEach(t -> {
                t.setPaid(false);
                t.setPaymentDate(null);
                repository.save(t);
            });
        }

        super.deleteById(id);
    }

    private void deleteItem(TransactionEntity invoiceItem) {
        TransactionEntity existingInvoice = repository.findById(invoiceItem.getInvoiceId())
                .orElseThrow(() -> new NotificationException("Fatura não encontrada"));

        if (existingInvoice.getPaid()) throw new NotificationException("Não é permitido excluir lançamento. A fatura está paga.");

        List<TransactionEntity> invoiceItems = new ArrayList<>(repository.findBy(existingInvoice.getId())
                .stream()
                .filter(t -> !t.getInvoice())
                .toList());

        invoiceItems.remove(invoiceItem);
        BigDecimal value = recalculate(invoiceItems);
        existingInvoice.setValue(value);
        updateTransactionTypeAndTypeCategoryWhenCredit(value, existingInvoice);

        repository.save(existingInvoice);
        super.deleteById(invoiceItem.getId());
    }

    private void updateTransactionTypeAndTypeCategoryWhenCredit(BigDecimal value, TransactionEntity invoice) {
        TypeCategory type = TypeCategory.EXPENSE;
        if (value.compareTo(BigDecimal.ZERO) > 0) {
            type = TypeCategory.INCOME;
        }
        invoice.setTransactionType(type.getTransactionType().name());
        invoice.setCategoryType(type.name());
    }
}
