package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.CardEntity;
import com.ctsousa.mover.core.entity.InvoicePaymentDetailEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.core.util.NumberUtil;
import com.ctsousa.mover.repository.InvoiceRepository;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.CardService;
import com.ctsousa.mover.service.InvoicePaymentService;
import com.ctsousa.mover.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.ctsousa.mover.core.util.DateUtil.monthInFull;
import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;
import static com.ctsousa.mover.core.util.StringUtil.removeDuplicateWords;
import static java.util.UUID.randomUUID;

@Component
public class InvoiceServiceImpl extends BaseServiceImpl<TransactionEntity, Long> implements InvoiceService {

    private static final String name = "FATURA CARTÃO %s %s %d";

    @Autowired
    private InvoiceRepository repository;
    private final CardService cardService;
    private final InvoicePaymentService invoicePaymentService;

    public InvoiceServiceImpl(TransactionRepository repository, CardService cardService, InvoicePaymentService invoicePaymentService) {
        super(repository);
        this.cardService = cardService;
        this.invoicePaymentService = invoicePaymentService;
    }

    @Override
    public TransactionEntity toGenerate(TransactionEntity entity) {

        if (hasNotCard(entity)) return entity;

        CardEntity card = cardService.findById(entity.getCard().getId());
        LocalDate dueDate = entity.getDueDate();

        TransactionEntity invoiceFound = repository.findBy(dueDate, card);

        if (invoiceFound != null) {
            BigDecimal value = invoiceFound.getValue().add(entity.getValue());
            invoiceFound.setTransactionType(value.compareTo(BigDecimal.ZERO) > 0 ? "CREDIT" : "DEBIT");
            invoiceFound.setValue(value);
            return save(invoiceFound);
        }

        return save(create(entity, createDescription(card, dueDate), dueDate));
    }

    @Override
    public void deleteById(Long id) {
        TransactionEntity entity = findById(id);
        TransactionEntity invoice = findById(entity.getInvoiceId());

        BigDecimal value = invoice.getValue().add(invertSignal(entity.getValue()));
        invoice.setValue(value);
        invoice.setTransactionType(value.compareTo(BigDecimal.ZERO) > 0 ? "CREDIT" : "DEBIT");
        save(invoice);

        super.deleteById(entity.getId());
    }

    @Override
    public TransactionEntity update(TransactionEntity invoice, TransactionEntity entity) {
        TransactionEntity savedEntity = findById(entity.getId());

        entity.setSignature(savedEntity.getSignature());
        invoice.setValue(calculateUpdatedValue(invoice.getValue(), savedEntity.getValue(), entity.getValue()));

        repository.save(invoice);
        return repository.save(entity);
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
        entities.forEach(this::save);
        return entities.stream().filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotificationException("Não existe fatura para ser realizado agendamento."));
    }

    @Override
    public TransactionEntity undoScheduling(Long id) {
        List<TransactionEntity> entities = getInvoice(id, false);
        entities.forEach(this::save);
        return entities.stream().filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotificationException("Não existe fatura para ser desfazer o agendamento."));
    }

    @Override
    public TransactionEntity pay(Long id, LocalDate paymentDate, BigDecimal value, AccountEntity account) {
        List<TransactionEntity> entities = searchById(id);
        entities.forEach(t -> t.setPaid(true));

        TransactionEntity invoice = entities.stream().filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotificationException("Fatura não encontrada"));

        var residualValue = BigDecimal.ZERO;

        if (NumberUtil.nonZero(invoice.getResidualValue(), BigDecimal.ZERO)) {
            residualValue = invoice.getResidualValue().add(value);
            invoice.setResidualValue(residualValue);
        } else if (NumberUtil.nonZero(invoice.getValue(), value)) {
            residualValue = invoice.getValue().add(value);
            invoice.setResidualValue(residualValue);
        }

        TransactionEntity payment = invoicePaymentService.create(invoice, account, paymentDate, value);
        entities.forEach(this::save);

        return payment;
    }

    @Override
    public TransactionEntity refund(Long id) {
        List<TransactionEntity> entities = searchById(id);
        entities.forEach(t -> t.setPaid(false));

        TransactionEntity invoice = entities.stream().filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotificationException("Fatura não encontrada"));

        invoice.setResidualValue(BigDecimal.ZERO);
        invoice.setRefund(true);
        entities.forEach(this::save);

        List<InvoicePaymentDetailEntity> details = invoicePaymentService.findByPaymentDetails(invoice.getId());
        details.forEach(detail -> invoicePaymentService.deletePaymentDetail(detail.getId()));
        details.forEach(detail -> super.deleteById(detail.getPayment().getId()));

        return invoice;
    }

    private void sendNext(TransactionEntity invoice) {

    }

    private String createDescription(CardEntity card, LocalDate dueDate) {
        return removeDuplicateWords(String.format(name, card.getName(), monthInFull(dueDate), dueDate.getYear()));
    }

    private List<TransactionEntity> getInvoice(Long id, boolean schedule) {
        List<TransactionEntity> entities = searchById(id);
        entities.forEach(t -> t.setScheduled(schedule));
        return entities;
    }

    private Boolean hasNotCard(TransactionEntity entity) {
        return entity.getCard() == null;
    }

    private TransactionEntity create(TransactionEntity entity, String description,  LocalDate dueDate) {
        TransactionEntity invoice = new TransactionEntity();
        invoice.setDescription(description);
        invoice.setSubcategory(entity.getSubcategory());
        invoice.setInstallment(0);
        invoice.setCategoryType(entity.getCategoryType());
        invoice.setDueDate(dueDate);
        invoice.setPaymentDate(entity.getPaymentDate());
        invoice.setRegisterDate(entity.getRegisterDate());
        invoice.setValue(entity.getValue());
        invoice.setCard(entity.getCard());
        invoice.setAccount(entity.getAccount());
        invoice.setVehicle(null);
        invoice.setContract(null);
        invoice.setSignature(String.valueOf(randomUUID()));
        invoice.setTransactionType(entity.getTransactionType());
        invoice.setPartner(null);
        invoice.setPaid(false);
        invoice.setRefund(entity.getRefund());
        invoice.setHour(entity.getHour());
        invoice.setLastInstallment(false);
        invoice.setScheduled(entity.getScheduled());
        invoice.setInvoiceId(null);
        invoice.setInvoice(true);
        invoice.setPredicted(false);
        invoice.setActive(true);
        return invoice;
    }

    private TransactionEntity create(TransactionEntity entity, BigDecimal value, String description,  LocalDate dueDate) {
        TransactionEntity invoice = create(entity, description, dueDate);
        invoice.setValue(value);
        return invoice;
    }

    private BigDecimal calculateUpdatedValue(BigDecimal invoiceValue, BigDecimal previousValue, BigDecimal newValue) {
        return invoiceValue.subtract(previousValue).add(newValue);
    }
}
