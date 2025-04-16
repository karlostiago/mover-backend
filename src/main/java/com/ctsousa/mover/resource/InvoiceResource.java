package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.InvoiceApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.*;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.security.Security;
import com.ctsousa.mover.core.util.DateUtil;
import com.ctsousa.mover.domain.InvoicePaymentDetail;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.Icon;
import com.ctsousa.mover.request.TransactionRequest;
import com.ctsousa.mover.response.InvoicePaymentDetailResponse;
import com.ctsousa.mover.response.TransactionResponse;
import com.ctsousa.mover.service.InvoicePaymentService;
import com.ctsousa.mover.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/invoices")
public class InvoiceResource extends BaseResource<TransactionResponse, TransactionRequest, TransactionEntity> implements InvoiceApi {

    @Autowired
    private InvoiceService invoiceService;

    private final InvoicePaymentService invoicePaymentService;

    public InvoiceResource(InvoiceService invoiceService, InvoicePaymentService invoicePaymentService) {
        super(invoiceService);
        this.invoicePaymentService = invoicePaymentService;
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.REGISTER_TRANSACTIONS)
    public ResponseEntity<TransactionResponse> add(TransactionRequest request) {
        throw new NotificationException("Ação não suportada.");
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.UPDATE_TRANSACTIONS)
    public ResponseEntity<TransactionResponse> update(Long id, TransactionRequest request) {
        TransactionEntity invoice = invoiceService.findById(request.getInvoiceId());
        Transaction domain = toMapper(request, Transaction.class);
        TransactionEntity updatedEntity = invoiceService.update(invoice, domain.toEntity());
        return ResponseEntity.ok(toMapper(updatedEntity, TransactionResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.FILTER_TRANSACTIONS)
    public ResponseEntity<List<TransactionResponse>> searchById(Long id) {
        List<TransactionEntity> entities = invoiceService.searchById(id);
        List<TransactionResponse> responses = toCollection(entities, TransactionResponse.class);
        updateResponse(responses, entities);
        return ResponseEntity.ok(responses);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.SCHEDULE_TRANSACTIONS)
    public ResponseEntity<TransactionResponse> schedule(Long id) {
        TransactionEntity entity = invoiceService.schedule(id);
        return ResponseEntity.ok(toMapper(entity, TransactionResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.UNDO_SCHEDULING_TRANSACTIONS)
    public ResponseEntity<TransactionResponse> undoSchedule(Long id) {
        TransactionEntity entity = invoiceService.undoScheduling(id);
        return ResponseEntity.ok(toMapper(entity, TransactionResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.PAYMENT_TRANSACTIONS)
    public ResponseEntity<TransactionResponse> pay(Long id, LocalDate paymentDate, TransactionRequest request) {
        AccountEntity account = new AccountEntity(request.getAccountId());
        TransactionEntity entity = invoiceService.pay(id, paymentDate, request.getValue(), account);
        return ResponseEntity.ok(toMapper(entity, TransactionResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.REFUND_TRANSACTIONS)
    public ResponseEntity<TransactionResponse> refund(Long id) {
        TransactionEntity entity = invoiceService.refund(id);
        return ResponseEntity.ok(toMapper(entity, TransactionResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.FILTER_TRANSACTIONS)
    public ResponseEntity<List<InvoicePaymentDetailResponse>> invoicePaymentDetail(Long id) {
        List<InvoicePaymentDetailEntity> entities = invoicePaymentService.findByPaymentDetails(id);
        List<InvoicePaymentDetailResponse> response = new ArrayList<>(entities.size());
        entities.forEach(detail -> {
            response.add(InvoicePaymentDetailResponse
                    .builder()
                            .id(detail.getId())
                            .value(detail.getValue())
                            .date(detail.getDate())
                            .account(detail.getAccount().getName())
                    .build());
        });
        return ResponseEntity.ok(response);
    }

    @Override
    public void updateResponse(List<TransactionResponse> response, List<TransactionEntity> entities) {
        Map<Long, TransactionResponse> responseMap = response.stream()
                .collect(Collectors.toMap(TransactionResponse::getId, r -> r, (existing, replacement) -> existing));

        for (TransactionEntity entity : entities) {
            SubCategoryEntity subcategory = entity.getSubcategory();
            CategoryEntity category = subcategory.getCategory();
            AccountEntity account = entity.getAccount();
            VehicleEntity vehicle = entity.getVehicle();
            CardEntity card = entity.getCard();
            ContractEntity contract = entity.getContract();

            TransactionResponse transactionResponse = responseMap.get(entity.getId());
            transactionResponse.setSubcategory(subcategory.getDescription());
            transactionResponse.setCategory(category.getDescription());
            transactionResponse.setAccount(String.format("%s - %s", account.getName(), account.getNumber()));

            if (entity.getInvoice()) {
                BigDecimal amountPaid = invoicePaymentService.findByPaymentDetails(entity.getId())
                        .stream().map(InvoicePaymentDetailEntity::getValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                transactionResponse.setAmountPaid(amountPaid);
            }

            Optional.ofNullable(vehicle).ifPresent(v -> transactionResponse.setVehicle(
                    String.format("%s - %s - %s", v.getBrand().getName(), v.getModel().getName(), v.getLicensePlate())
            ));

            Optional.ofNullable(card).ifPresent(c -> transactionResponse.setCard(c.getName()));
            Optional.ofNullable(contract).ifPresent(c -> transactionResponse.setContract(c.getNumber()));

            transactionResponse.setDate(Optional.ofNullable(transactionResponse.getPaymentDate())
                    .orElse(transactionResponse.getDueDate()));

            transactionResponse.setDayOfWeek(DateUtil.dayOfWeek(transactionResponse.getRegisterDate()));
            transactionResponse.setIcon(Icon.toName(account.getIcon()).getUrlImage());
        }
    }

    @Override
    public Class<?> responseClass() {
        return TransactionResponse.class;
    }
}
