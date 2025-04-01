package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.TransactionApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.*;
import com.ctsousa.mover.core.security.Security;
import com.ctsousa.mover.core.util.DateUtil;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.Icon;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.request.TransactionRequest;
import com.ctsousa.mover.response.TransactionResponse;
import com.ctsousa.mover.service.CardService;
import com.ctsousa.mover.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/transactions")
public class TransactionResource extends BaseResource<TransactionResponse, TransactionRequest, TransactionEntity> implements TransactionApi {

    private static Long REMAINING_PAGE = 0L;

    @Autowired
    private TransactionService transactionService;

    private final CardService cardService;

    public TransactionResource(TransactionService transactionService, CardService cardService) {
        super(transactionService);
        this.cardService = cardService;
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.REGISTER_TRANSACTIONS)
    public ResponseEntity<TransactionResponse> add(TransactionRequest request) {
        Transaction domain = toMapper(request, Transaction.class);
        TransactionEntity entity = transactionService.save(domain);
        return ResponseEntity.ok(toMapper(entity, TransactionResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.UPDATE_TRANSACTIONS)
    public ResponseEntity<TransactionResponse> update(Long id, TransactionRequest request) {
        transactionService.existsById(id);
        Transaction domain = toMapper(request, Transaction.class);
        TransactionEntity entity = transactionService.update(domain);
        return ResponseEntity.ok(toMapper(entity, TransactionResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.FILTER_TRANSACTIONS)
    public ResponseEntity<TransactionResponse> findById(Long id) {
        TransactionEntity entity = transactionService.findById(id);
        TypeCategory type = TypeCategory.toDescription(entity.getCategoryType());
        TransactionResponse response = toMapper(transactionService.filterById(id, type), TransactionResponse.class);
        updateResponse(Collections.singletonList(response), Collections.singletonList(entity));
        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.FILTER_TRANSACTIONS)
    public ResponseEntity<List<TransactionResponse>> searchInvoice(Long id) {
        List<TransactionEntity> entities = transactionService.searchInvoiceBy(id);
        List<TransactionResponse> responses = toCollection(entities, TransactionResponse.class);
        updateResponse(responses, entities);
        return ResponseEntity.ok(responses);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.PAYMENT_TRANSACTIONS)
    public ResponseEntity<TransactionResponse> pay(Long id, LocalDate paymentDate) {
        TransactionEntity entity = transactionService.pay(id, paymentDate);
        return ResponseEntity.ok(toMapper(entity, TransactionResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.REFUND_TRANSACTIONS)
    public ResponseEntity<TransactionResponse> refund(Long id) {
        TransactionEntity entity = transactionService.refund(id);
        return ResponseEntity.ok(toMapper(entity, TransactionResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.DELETE_TRANSACTIONS)
    public void batchDelete(Long id) {
        transactionService.batchDelete(id);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.UPDATE_TRANSACTIONS)
    public ResponseEntity<TransactionResponse> batchUpdate(Long id, TransactionRequest request) {
        transactionService.existsById(id);
        Transaction domain = toMapper(request, Transaction.class);
        TransactionEntity entity = transactionService.batchUpdate(id, domain);
        return ResponseEntity.ok(toMapper(entity, TransactionResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.FILTER_TRANSACTIONS)
    public ResponseEntity<List<TransactionResponse>> filterBy(String uri) {
        var filter = new Transaction.Filter(uri);

        Page<TransactionEntity> page = transactionService.search(filter, PageRequest.of(filter.getPageNumber(), 100));

        REMAINING_PAGE = BigDecimal.valueOf(page.getTotalPages() - (page.getNumber() + 1)).longValue();

        List<TransactionEntity> entities = page.stream().toList();
        List<TransactionResponse> responses = toCollection(entities, TransactionResponse.class);
        updateResponse(responses, entities);

        return ResponseEntity.ok(responses);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.FILTER_TRANSACTIONS)
    public ResponseEntity<List<TransactionResponse>> findAll() {
        return super.findAll();
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.SCHEDULE_TRANSACTIONS)
    public ResponseEntity<TransactionResponse> schedule(Long id) {
        TransactionEntity entity = transactionService.schedule(id);
        return ResponseEntity.ok(toMapper(entity, TransactionResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.UNDO_SCHEDULING_TRANSACTIONS)
    public ResponseEntity<TransactionResponse> undoSchedule(Long id) {
        TransactionEntity entity = transactionService.undoScheduling(id);
        return ResponseEntity.ok(toMapper(entity, TransactionResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Transaction.CALCULATE_CUT_OFF_DATE_TRANSACTIONS)
    public ResponseEntity<TransactionResponse> calculateCutOffDate(TransactionRequest request) {
        Transaction domain = toMapper(request, Transaction.class);
        CardEntity card = cardService.findById(domain.getCard().getId());
        TransactionEntity entity = domain.toEntity();
        entity.setDueDate(cardService.calculateCutOffDate(card, entity.getRegisterDate()));
        return ResponseEntity.ok(toMapper(entity, TransactionResponse.class));
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
//            TransactionEntity.Invoice invoice = entity.getInvoice();

            TransactionResponse transactionResponse = responseMap.get(entity.getId());
            transactionResponse.setSubcategory(subcategory.getDescription());
            transactionResponse.setCategory(category.getDescription());
            transactionResponse.setRemainingPages(REMAINING_PAGE);
            transactionResponse.setAccount(String.format("%s - %s", account.getName(), account.getNumber()));

            Optional.ofNullable(vehicle).ifPresent(v -> transactionResponse.setVehicle(
                    String.format("%s - %s - %s", v.getBrand().getName(), v.getModel().getName(), v.getLicensePlate())
            ));

            Optional.ofNullable(card).ifPresent(c -> transactionResponse.setCard(c.getName()));
            Optional.ofNullable(contract).ifPresent(c -> transactionResponse.setContract(c.getNumber()));

            transactionResponse.setDate(Optional.ofNullable(transactionResponse.getPaymentDate())
                    .orElse(transactionResponse.getDueDate()));

            transactionResponse.setDayOfWeek(DateUtil.dayOfWeek(transactionResponse.getDate()));
            transactionResponse.setIcon(Icon.toName(account.getIcon()).getUrlImage());

//            Optional.ofNullable(invoice).ifPresent(inv -> {
//                transactionResponse.setInvoice(new InvoiceResponse(inv));
//                transactionResponse.setHasInvoice(true);
//                transactionResponse.setScheduled(null);
//            });
        }
    }

    @Override
    public Class<?> responseClass() {
        return TransactionResponse.class;
    }
}
