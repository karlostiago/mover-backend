package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.InvoicePaymentDetailEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoicePaymentDetailRepository extends JpaRepository<InvoicePaymentDetailEntity, Long> {

    @Query("SELECT i FROM InvoicePaymentDetailEntity i WHERE i.payment.id = :id")
    List<InvoicePaymentDetailEntity> findByPaymentId(@NonNull Long id);

    @Query("""
            SELECT i FROM InvoicePaymentDetailEntity i
            JOIN FETCH i.invoice
            JOIN FETCH i.payment
            JOIN FETCH i.account
            WHERE i.invoice.id = :id
            """)
    List<InvoicePaymentDetailEntity> findByInvoiceId(@NonNull Long id);

    @NonNull
    @Override
    @Query("""
            SELECT i FROM InvoicePaymentDetailEntity i
            JOIN FETCH i.invoice
            JOIN FETCH i.payment
            JOIN FETCH i.account
            WHERE i.id = :id
            """)
    Optional<InvoicePaymentDetailEntity> findById(@NonNull Long id);
}
