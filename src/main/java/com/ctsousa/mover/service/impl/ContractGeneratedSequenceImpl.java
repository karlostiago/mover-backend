package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.enumeration.PaymentFrequency;
import com.ctsousa.mover.enumeration.Situation;
import com.ctsousa.mover.response.ContractResponse;
import com.ctsousa.mover.service.ContractGeneratedSequenceService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class ContractGeneratedSequenceImpl implements ContractGeneratedSequenceService {

    private final Map<LocalDate, Integer> sequenceByDate = new HashMap<>();
    private LocalDate lastDate = LocalDate.now();

    @Override
    public ContractResponse generatedNewContractWithSequence() {
        LocalDate today = LocalDate.now();
        if (!today.equals(lastDate)) {
            sequenceByDate.clear();
            lastDate = today;
        }
        int sequence = sequenceByDate.getOrDefault(today, 0) + 1;
        sequenceByDate.put(today, sequence);
        ContractResponse response = new ContractResponse();
        response.setNumber(String.format("%s-%03d", today.toString().replace("-", ""), sequence));
        response.setInitialDate(LocalDate.now());
        response.setSituation(Situation.ONGOING);
        response.setActive(Boolean.TRUE);
        response.setPaymentFrequency(PaymentFrequency.WEEKLY);
        return response;
    }
}
