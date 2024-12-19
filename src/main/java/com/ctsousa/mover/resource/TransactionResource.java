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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/transactions")
public class TransactionResource extends BaseResource<TransactionResponse, TransactionRequest, TransactionEntity> implements TransactionApi {

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
    public ResponseEntity<TransactionResponse> pay(Long id) {
        TransactionEntity entity = transactionService.pay(id);
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
    public ResponseEntity<List<TransactionResponse>> filterBy(String search) {
        return null;
    }

    @Override
    public ResponseEntity<BalanceResponse> balance() {
        BalanceResponse response = new BalanceResponse();
        response.setCurrentAccount(transactionService.accountBalace());
        response.setIncome(transactionService.incomeBalance());
        response.setExpense(transactionService.expenseBalance());
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
