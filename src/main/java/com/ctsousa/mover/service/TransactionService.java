package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.service.BaseService;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TypeCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService extends BaseService<TransactionEntity, Long> {

    TransactionEntity save(final Transaction transaction);

    TransactionEntity update(final Transaction transaction);

    TransactionEntity filterById(final Long id, TypeCategory typeCategory);

    TransactionEntity pay(final Long id, LocalDate paymentDate);

    TransactionEntity refund(final Long id);

    void batchDelete(final Long id);

    TransactionEntity batchUpdate(final Long id, Transaction transaction);

//    BigDecimal accountBalace(final List<Long> listAccountId);
//
//    BigDecimal incomeBalance(final List<TransactionEntity> entities);
//
//    BigDecimal expenseBalance(final List<TransactionEntity> entities);
//
//    TransactionResponse searchById(final Long id);
//
//    void deleteById(Long id, Boolean deleteOnlyThis);
//
//    void batchUpdate(Transaction transaction);
//
    Page<TransactionEntity> search(final LocalDate dtInitial, final LocalDate dtFinal, List<Long> accountListId, String text, Pageable pageable);
}
