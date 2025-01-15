package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.TransactionApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.BankIcon;
import com.ctsousa.mover.request.TransactionRequest;
import com.ctsousa.mover.response.BalanceResponse;
import com.ctsousa.mover.response.TransactionResponse;
import com.ctsousa.mover.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<TransactionResponse> add(TransactionRequest request) {
        Transaction domain = toMapper(request, Transaction.class);
        TransactionEntity entity = transactionService.save(domain);
        return ResponseEntity.ok(toMapper(entity, TransactionResponse.class));
    }

    @Override
    public ResponseEntity<TransactionResponse> update(Long id, TransactionRequest request) {
        transactionService.existsById(id);
        Transaction domain = toMapper(request, Transaction.class);
        return ResponseEntity.ok(transactionService.update(id, domain));
    }

    @Override
    public ResponseEntity<TransactionResponse> pay(Long id, LocalDate paymentDate) {
        TransactionEntity entity = transactionService.pay(id, paymentDate);
        return ResponseEntity.ok(toMapper(entity, TransactionResponse.class));
    }

    @Override
    public ResponseEntity<TransactionResponse> refund(Long id) {
        TransactionEntity entity = transactionService.refund(id);
        return ResponseEntity.ok(toMapper(entity, TransactionResponse.class));
    }

    @Override
    public ResponseEntity<TransactionResponse> findById(Long id) {
        return ResponseEntity.ok(transactionService.searchById(id));
    }

    @Override
    public void delete(Long id, Boolean deleteOnlyThis) {
        transactionService.deleteById(id, deleteOnlyThis);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> filterBy(String uri) {

        var filter = new Transaction.Filter(uri);

        Page<TransactionEntity> page = transactionService.find(filter.getDtInitial(), filter.getDtFinal(), filter.getAccountsId(),
                filter.getText(), PageRequest.of(filter.getPageNumber(), 100));

        REMAINING_PAGE = BigDecimal.valueOf(page.getTotalPages() - (page.getNumber() + 1)).longValue();

        List<TransactionEntity> entities = page.stream().toList();
        List<TransactionResponse> responses = toCollection(entities, TransactionResponse.class);
        updateResponse(responses, entities);

        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<BalanceResponse> balance(String uri) {
        var filter = new Transaction.Filter(uri);
        BalanceResponse response = new BalanceResponse();
        Page<TransactionEntity> page = transactionService.find(filter.getDtInitial(), filter.getDtFinal(), filter.getAccountsId(),
                filter.getText(), PageRequest.of(0, Integer.MAX_VALUE));

        response.setCurrentAccount(transactionService.accountBalace(filter.getAccountsId()));
        response.setIncome(transactionService.incomeBalance(page.stream().toList()));
        response.setExpense(transactionService.expenseBalance(page.stream().toList()));
        response.setGeneralBalance(response.getIncome().subtract(response.getExpense()));

        return ResponseEntity.ok(response);
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
            transactionResponse.setIcon(icon.getImage());
        }
    }

    @Override
    public Class<?> responseClass() {
        return TransactionResponse.class;
    }
}
