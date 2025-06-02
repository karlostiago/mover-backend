package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.CardEntity;
import com.ctsousa.mover.core.entity.InvoicePaymentDetailEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.core.util.NumberUtil;
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
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ctsousa.mover.core.util.DateUtil.monthInFull;
import static com.ctsousa.mover.core.util.NumberUtil.*;
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
    public TransactionEntity toGenerate(TransactionEntity entity) {

        if (hasNotCard(entity)) return entity;

        CardEntity card = cardService.findById(entity.getCard().getId());
        LocalDate dueDate = entity.getDueDate();

        TransactionEntity invoiceFound = repository.findBy(dueDate, card);

        if (invoiceFound != null) {
            BigDecimal value = invoiceFound.getValue().add(entity.getValue());
            updateTransactionTypeAndTypeCategoryWhenCredit(value, invoiceFound);
            invoiceFound.setValue(value);
            return save(invoiceFound);
        }

        return save(create(entity, createDescription(card, dueDate)));
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
    public TransactionEntity update(TransactionEntity invoice, TransactionEntity entity) {
        TransactionEntity savedEntity = findById(entity.getId());
        BigDecimal newValue = entity.getValue();
        if (entity.getCard() == null) {
            entity.setInvoiceId(null);
            newValue = BigDecimal.ZERO;
        }
        entity.setSignature(savedEntity.getSignature());
        invoice.setValue(calculateUpdatedValue(invoice.getValue(), savedEntity.getValue(), newValue));
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
                .orElseThrow(() -> new NotificationException("Não existe fatura para realizar agendamento."));
    }

    @Override
    public TransactionEntity undoScheduling(Long id) {
        List<TransactionEntity> entities = getInvoice(id, false);
        entities.forEach(this::save);
        return entities.stream().filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotificationException("Não existe fatura para desfazer agendamento."));
    }

    @Override
    public TransactionEntity pay(Long id, LocalDate paymentDate, BigDecimal value, AccountEntity account) {
        List<TransactionEntity> entities = searchById(id);
        entities.forEach(t -> t.setPaid(true));

        TransactionEntity invoice = entities.stream().filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotificationException("Fatura não encontrada"));

        BigDecimal residualValue = calculateResidualValue(invoice, value);
        invoice.setResidualValue(residualValue);

        TransactionEntity payment = invoicePaymentService.create(invoice, account, paymentDate, value);
        entities.forEach(this::save);

        handleResidualValue(payment, invoice.getCard(), residualValue);

        return payment;
    }

    @Override
    public TransactionEntity refund(Long id) {
        List<TransactionEntity> entities = searchById(id);

        if (!entities.isEmpty()) {
            updateRefundAndPaidAndResidualValue(entities);
            TransactionEntity invoice = entities.stream()
                    .filter(t -> t.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new NotificationException("Fatura não encontrada"));

            entities.forEach(this::save);

            invoicePaymentService.findByPaymentDetails(invoice.getId())
                    .forEach(detail -> {
                        invoicePaymentService.deletePaymentDetail(detail.getId());
                        super.deleteById(detail.getPayment().getId());
                    });

            return invoice;
        }

        InvoicePaymentDetailEntity paymentDetail = invoicePaymentDetailRepository.findById(id)
                .orElseThrow(() -> new NotificationException("Detalhe de pagamento não encontrado"));

        List<InvoicePaymentDetailEntity> details = invoicePaymentDetailRepository.findByInvoiceId(paymentDetail.getInvoice().getId());
        entities = searchById(paymentDetail.getInvoice().getId());

        details.removeIf(detail -> detail.getId().equals(paymentDetail.getId()));

        invoicePaymentDetailRepository.delete(paymentDetail);
        invoicePaymentService.deleteById(paymentDetail.getPayment().getId());

        if (details.isEmpty()) {
            updateRefundAndPaidAndResidualValue(entities);
            entities.forEach(this::save);
        }

        return paymentDetail.getInvoice();
    }

    private void updateRefundAndPaidAndResidualValue(List<TransactionEntity> entities) {
        entities.forEach(t -> {
            t.setRefund(true);
            t.setPaid(false);
            if (Boolean.TRUE.equals(t.getInvoice())) {
                t.setResidualValue(BigDecimal.ZERO);
            }
        });
    }

    private void handleResidualValue(TransactionEntity invoice, CardEntity card, BigDecimal residualValue) {
        LocalDate dueDate = invoice.getDueDate().plusMonths(1);
        TransactionEntity nextInvoice = repository.findBy(dueDate, card);
        TypeCategory typeCategory = NumberUtil.lessThanZero(residualValue) ? TypeCategory.EXPENSE : TypeCategory.INCOME;

        if (nextInvoice == null) {
            if (equalsZero(residualValue)) return;
            TransactionEntity newInvoice = new TransactionEntity();
            BeanUtils.copyProperties(invoice, newInvoice);
            newInvoice.setId(null);
            newInvoice.setDescription(createDescription(newInvoice.getCard(), dueDate));
            newInvoice.setDueDate(dueDate);
            newInvoice.setPaymentDate(null);
            newInvoice.setPaid(false);
            newInvoice.setCard(card);
            newInvoice.setResidualValue(BigDecimal.ZERO);
            newInvoice.setRegisterDate(LocalDate.now());
            newInvoice.setPredicted(true);
            newInvoice.setRefund(false);
            newInvoice.setScheduled(false);
            newInvoice.setTransactionType(typeCategory.getTransactionType().name());
            newInvoice.setCategoryType(typeCategory.name());
            newInvoice.setHour(LocalTime.now());
            newInvoice.setSignature(String.valueOf(UUID.randomUUID()));
            newInvoice.setValue(residualValue);
            save(newInvoice);
        } else {
            BigDecimal value = nextInvoice.getValue().add(invoice.getValue().abs());
            nextInvoice.setValue(value);
            nextInvoice.setResidualValue(nextInvoice.getResidualValue().add(residualValue));
            nextInvoice.setTransactionType(typeCategory.getTransactionType().name());
            nextInvoice.setCategoryType(typeCategory.name());

            if (NumberUtil.equalsZero(value)) {
                super.deleteById(nextInvoice.getId());
            } else {
                save(nextInvoice);
            }
        }
    }

    private BigDecimal calculateResidualValue(TransactionEntity invoice, BigDecimal value) {
        var residualValue = BigDecimal.ZERO;
        if (nonZero(invoice.getResidualValue())) {
            return invoice.getResidualValue().add(value);
        } else if (nonZero(invoice.getValue(), value)) {
            return invoice.getValue().add(value);
        }
        return residualValue;
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
        return invoice;
    }

    private BigDecimal calculateUpdatedValue(BigDecimal invoiceValue, BigDecimal previousValue, BigDecimal newValue) {
        return invoiceValue.subtract(previousValue).add(newValue);
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
            entities.forEach(e -> e.setPaid(false));
            entities.forEach(this::save);
        }

        super.deleteById(id);
    }

    private void deleteItem(TransactionEntity entity) {
        TransactionEntity invoice = findById(entity.getInvoiceId());

        if (invoice.getPaid()) throw new NotificationException("Não é permitido excluir lançamento. A fatura está paga.");

        invoice.setValue(invoice.getValue().add(invertSignal(entity.getValue())));
        updateTransactionTypeAndTypeCategoryWhenCredit(invoice.getValue(), invoice);
        if (invoice.getValue().compareTo(BigDecimal.ZERO) == 0) {
            super.deleteById(invoice.getId());
        } else {
            save(invoice);
            super.deleteById(entity.getId());
        }
    }

    private void updateTransactionTypeAndTypeCategoryWhenCredit(BigDecimal value, TransactionEntity invoiceFound) {
        TypeCategory type = TypeCategory.EXPENSE;
        if (value.compareTo(BigDecimal.ZERO) > 0) {
            type = TypeCategory.INCOME;
        }
        invoiceFound.setTransactionType(type.getTransactionType().name());
        invoiceFound.setCategoryType(type.name());
    }
}
