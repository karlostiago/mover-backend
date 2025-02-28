package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.TransactionApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.security.Security;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.BankIcon;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.request.TransactionRequest;
import com.ctsousa.mover.response.TransactionResponse;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/transactions")
public class TransactionResource extends BaseResource<TransactionResponse, TransactionRequest, TransactionEntity> implements TransactionApi {

    private static Long REMAINING_PAGE = 0L;

    @Autowired
    private TransactionService transactionService;

    public TransactionResource(TransactionService transactionService) {
        super(transactionService);
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
        return ResponseEntity.ok(toMapper(transactionService.filterById(id, type), TransactionResponse.class));
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

        Page<TransactionEntity> page = transactionService.search(filter.getDtInitial(), filter.getDtFinal(), filter.getAccountsId(),
                filter.getText(), PageRequest.of(filter.getPageNumber(), 100));

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
    public void updateResponse(List<TransactionResponse> response, List<TransactionEntity> entities) {
        Map<Long, TransactionResponse> responseMap = response.stream()
                .collect(Collectors.toMap(TransactionResponse::getId, r -> r));

        for (TransactionEntity entity : entities) {
            String subcategory = entity.getSubcategory().getDescription();
            String category = entity.getSubcategory().getCategory().getDescription();
            TransactionResponse transactionResponse = responseMap.get(entity.getId());
            transactionResponse.setSubcategory(subcategory);
            transactionResponse.setCategory(category);
            transactionResponse.setRemainingPages(REMAINING_PAGE);

            if (transactionResponse.getPaymentDate() != null) {
                transactionResponse.setDate(transactionResponse.getPaymentDate());
            } else {
                transactionResponse.setDate(transactionResponse.getDueDate());
            }

            if ("TRANSFER".equals(entity.getCategoryType())) {
                transactionResponse.setValue(entity.getValue().multiply(BigDecimal.valueOf(-1D)));
            }

            BankIcon icon = BankIcon.toName(entity.getAccount().getIcon());
            transactionResponse.setIcon(icon.getUrlImage());
        }
    }

    @Override
    public Class<?> responseClass() {
        return TransactionResponse.class;
    }
}
