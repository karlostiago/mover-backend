package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.InvoicePaymentDetailEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.repository.InvoicePaymentDetailRepository;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.InvoicePaymentService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;
import static java.util.UUID.randomUUID;

@Component
public class InvoicePaymentImpl extends BaseServiceImpl<TransactionEntity, Long> implements InvoicePaymentService {

    private final InvoicePaymentDetailRepository invoicePaymentDetailRepository;

    public InvoicePaymentImpl(TransactionRepository repository, InvoicePaymentDetailRepository invoicePaymentDetailRepository) {
        super(repository);
        this.invoicePaymentDetailRepository = invoicePaymentDetailRepository;
    }

    @Override
    public Long findPaymentId(Long id) {
        try {
            InvoicePaymentDetailEntity detailEntity = invoicePaymentDetailRepository.findByPaymentId(id)
                    .stream().findFirst()
                    .orElseThrow(() -> new NotificationException("Não existe detalhe pagamento.", Severity.INFO));
            return detailEntity.getInvoice().getId();
        } catch (NotificationException e) {
            return id;
        }
    }

    @Override
    public List<InvoicePaymentDetailEntity> findByPaymentDetails(Long invoiceId) {
        return invoicePaymentDetailRepository.findByInvoiceId(invoiceId);
    }

    @Override
    public void deletePaymentDetail(Long id) {
        InvoicePaymentDetailEntity entity = invoicePaymentDetailRepository.findById(id)
                .orElseThrow(() -> new NotificationException("Não foi encontrado registro detalhe de pagamento."));
        invoicePaymentDetailRepository.deleteById(entity.getId());
    }

    @Override
    public TransactionEntity create(TransactionEntity invoice, AccountEntity account, LocalDate paymentDate, BigDecimal value) {
        TransactionEntity payment = new TransactionEntity();
        payment.setDescription(invoice.getDescription().replace("FATURA CARTÃO", "PAGAMENTO FATURA CARTÃO"));
        payment.setSubcategory(invoice.getSubcategory());
        payment.setInstallment(0);
        payment.setCategoryType(invoice.getCategoryType());
        payment.setDueDate(invoice.getDueDate());
        payment.setPaymentDate(paymentDate);
        payment.setRegisterDate(LocalDate.now());
        payment.setValue(invertSignal(value));
        payment.setCard(invoice.getCard());
        payment.setAccount(account);
        payment.setVehicle(null);
        payment.setContract(null);
        payment.setSignature(String.valueOf(randomUUID()));
        payment.setTransactionType(TransactionType.DEBIT.name());
        payment.setPartner(null);
        payment.setPaid(true);
        payment.setRefund(false);
        payment.setHour(LocalTime.now());
        payment.setLastInstallment(false);
        payment.setScheduled(invoice.getScheduled());
        payment.setInvoiceId(null);
        payment.setInvoice(true);
        payment.setPredicted(false);
        payment.setActive(true);
        payment.setPaymentType("SINGLE");
        TransactionEntity savedPayment = save(payment);
        createPaymentDetail(savedPayment, invoice, value, account);
        return savedPayment;
    }

    private void createPaymentDetail(TransactionEntity payment, TransactionEntity invoice, BigDecimal value, AccountEntity account) {
        InvoicePaymentDetailEntity entity = new InvoicePaymentDetailEntity();
        entity.setValue(value);
        entity.setDate(payment.getPaymentDate());
        entity.setInvoice(invoice);
        entity.setPayment(payment);
        entity.setAccount(account);
        invoicePaymentDetailRepository.save(entity);
    }
}
