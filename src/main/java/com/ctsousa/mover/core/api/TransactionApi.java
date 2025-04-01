package com.ctsousa.mover.core.api;

import com.ctsousa.mover.request.TransactionRequest;
import com.ctsousa.mover.response.TransactionResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

public interface TransactionApi {

    @GetMapping("/filterBy")
    ResponseEntity<List<TransactionResponse>> filterBy(@RequestParam("search") String uri);

    @PutMapping("/{id}/pay/{paymentDate}")
    ResponseEntity<TransactionResponse> pay(@PathVariable Long id, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate paymentDate);

    @PutMapping("/{id}/refund")
    ResponseEntity<TransactionResponse> refund(@PathVariable Long id);

    @DeleteMapping("/batch-delete/{id}")
    void batchDelete(@PathVariable Long id);

    @PutMapping("/batch-update/{id}")
    ResponseEntity<TransactionResponse>  batchUpdate(@PathVariable Long id, @Valid  @RequestBody TransactionRequest request);

    @PutMapping("/{id}/schedule")
    ResponseEntity<TransactionResponse> schedule(@PathVariable Long id);

    @PutMapping("/{id}/undo-scheduling")
    ResponseEntity<TransactionResponse> undoSchedule(@PathVariable Long id);

    @PutMapping("/calculate-cut-off-date")
    ResponseEntity<TransactionResponse>  calculateCutOffDate(@RequestBody TransactionRequest request);

    @GetMapping("/invoice/{id}")
    ResponseEntity<List<TransactionResponse>> searchInvoice(@PathVariable Long id);
}
