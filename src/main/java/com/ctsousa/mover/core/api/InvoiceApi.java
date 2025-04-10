package com.ctsousa.mover.core.api;

import com.ctsousa.mover.request.TransactionRequest;
import com.ctsousa.mover.response.InvoicePaymentDetailResponse;
import com.ctsousa.mover.response.TransactionResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceApi {

    @GetMapping("/{id}/invoice-payment-detail")
    ResponseEntity<List<InvoicePaymentDetailResponse>> invoicePaymentDetail(@PathVariable Long id);

    @GetMapping("/invoice/{id}")
    ResponseEntity<List<TransactionResponse>> searchById(@PathVariable Long id);

    @PutMapping("/{id}/schedule")
    ResponseEntity<TransactionResponse> schedule(@PathVariable Long id);

    @PutMapping("/{id}/undo-scheduling")
    ResponseEntity<TransactionResponse> undoSchedule(@PathVariable Long id);

    @PutMapping("/{id}/pay/{paymentDate}")
    ResponseEntity<TransactionResponse> pay(@PathVariable Long id,
                                            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate paymentDate,
                                            @RequestBody TransactionRequest request);

    @PutMapping("/{id}/refund")
    ResponseEntity<TransactionResponse> refund(@PathVariable Long id);
}
