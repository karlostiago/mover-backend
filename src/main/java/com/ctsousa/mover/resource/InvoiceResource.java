package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.InvoiceApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.*;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.security.Security;
import com.ctsousa.mover.core.util.DateUtil;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.Icon;
import com.ctsousa.mover.request.TransactionRequest;
import com.ctsousa.mover.response.TransactionResponse;
import com.ctsousa.mover.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/invoices")
public class InvoiceResource extends BaseResource<TransactionResponse, TransactionRequest, TransactionEntity> implements InvoiceApi {

    @Autowired
    private InvoiceService invoiceService;

    public InvoiceResource(InvoiceService invoiceService) {
        super(invoiceService);
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

            Optional.ofNullable(vehicle).ifPresent(v -> transactionResponse.setVehicle(
                    String.format("%s - %s - %s", v.getBrand().getName(), v.getModel().getName(), v.getLicensePlate())
            ));

            Optional.ofNullable(card).ifPresent(c -> transactionResponse.setCard(c.getName()));
            Optional.ofNullable(contract).ifPresent(c -> transactionResponse.setContract(c.getNumber()));

            transactionResponse.setDate(Optional.ofNullable(transactionResponse.getPaymentDate())
                    .orElse(transactionResponse.getDueDate()));

            transactionResponse.setDayOfWeek(DateUtil.dayOfWeek(transactionResponse.getDate()));
            transactionResponse.setIcon(Icon.toName(account.getIcon()).getUrlImage());
        }
    }

    @Override
    public Class<?> responseClass() {
        return TransactionResponse.class;
    }
}
