package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.*;
import com.ctsousa.mover.enumeration.Icon;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.service.AccountService;
import com.ctsousa.mover.service.InvoiceService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ctsousa.mover.core.util.DateUtil.monthInFull;

@Component
public class InvoiceServiceImpl implements InvoiceService {

    private static final String INVOICE_TEXT = "FATURA";
    private static final String CARD_TEXT = "CARTÃO";

    @Override
    public List<TransactionEntity> genereteInvoice(final List<TransactionEntity> entities) {
        List<TransactionEntity> transactions = new ArrayList<>(entities.size());

        entities.stream().filter(entity -> entity.getCard() == null)
                .forEach(transactions::add);

        Map<CardEntity, List<TransactionEntity>> invoicesMap = entities.stream()
                .filter(entity -> entity.getCard() != null)
                .collect(Collectors.groupingBy(TransactionEntity::getCard));

        TransactionEntity.Invoice invoice;

        for (Map.Entry<CardEntity, List<TransactionEntity>> entry : invoicesMap.entrySet()) {
            List<TransactionEntity> values = entry.getValue();
            invoice = new TransactionEntity.Invoice();
            for (TransactionEntity entity : values) {
                invoice.add(entity);
            }
            transactions.add(getTransaction(invoice));
        }

        return transactions;
    }

    private TransactionEntity getTransaction(TransactionEntity.Invoice invoice) {
        SubCategoryEntity subcategory = new SubCategoryEntity();
        subcategory.setCategory(new CategoryEntity(INVOICE_TEXT, TypeCategory.EXPENSE, new ArrayList<>()));
        subcategory.setDescription(String.format("%s %s", INVOICE_TEXT, CARD_TEXT));

        TransactionEntity entity = new TransactionEntity();
        entity.setId(invoice.getCard().getId());
        entity.setDescription(String.format("%s %s - %s %d", INVOICE_TEXT, invoice.getCard().getName(), monthInFull(invoice.getDueDate()), invoice.getDueDate().getYear()));
        entity.setValue(invoice.getTotal());
        entity.setCard(invoice.getCard());
        entity.setDueDate(invoice.getDueDate());
        entity.setPaymentDate(invoice.getPaymentDate());
        entity.setTransactionType(TransactionType.DEBIT.name());
        entity.setSubcategory(subcategory);
        entity.setCard(invoice.getCard());

        AccountEntity account = new AccountEntity();
        account.setId(invoice.getCard().getAccount().getId());
        account.setIcon(Icon.INVOICE.name());

        entity.setAccount(account);
        entity.setInvoice(invoice);
        entity.setCategoryType(TypeCategory.EXPENSE.name());
        return entity;
    }
}
