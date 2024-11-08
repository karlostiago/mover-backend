package com.ctsousa.mover.core.api;

import com.ctsousa.mover.request.ContractRequest;
import com.ctsousa.mover.response.ContractResponse;
import com.ctsousa.mover.response.DayOfWeekResponse;
import com.ctsousa.mover.response.PaymentFrequencyResponse;
import com.ctsousa.mover.response.SituationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

public interface ContractApi {

    @GetMapping("/filterBy")
    ResponseEntity<List<ContractResponse>> filterBy(@RequestParam("search") String search);

    @GetMapping("/contract-by/{clientId}")
    ResponseEntity<Optional<ContractResponse>> contractBy(@PathVariable Long clientId);

    @GetMapping("/payment-frequencies")
    ResponseEntity<List<PaymentFrequencyResponse>> paymentFrequencies();

    @GetMapping("/situations")
    ResponseEntity<List<SituationResponse>> situations();

    @GetMapping("/days-of-week")
    ResponseEntity<List<DayOfWeekResponse>> daysOfWeek();

    @GetMapping("/generated-new-contract-with-sequence")
    ResponseEntity<ContractResponse> generatedSequence();

    @PutMapping("/{id}/close")
    ResponseEntity<ContractResponse> close(@PathVariable Long id, @RequestBody ContractRequest contractRequest);
}
