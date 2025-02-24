package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.enumeration.Situation;
import com.ctsousa.mover.repository.ContractRepository;
import com.ctsousa.mover.response.ContractResponse;
import com.ctsousa.mover.service.ContractGeneratedSequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class ContractGeneratedSequenceImpl implements ContractGeneratedSequenceService {

    private final Map<LocalDate, Integer> sequenceByDate = new HashMap<>();
    private LocalDate lastDate = LocalDate.now();

    @Autowired
    private ContractRepository repository;

    @Override
    public ContractResponse generatedNewContractWithSequence() {
        ContractResponse response = new ContractResponse();
        String sequentialNumber = createSequentialNumber();
        while (repository.existsByNumber(sequentialNumber)) {
            sequentialNumber = createSequentialNumber();
        }
        response.setNumber(sequentialNumber);
        response.setInitialDate(LocalDate.now());
        response.setSituation(Situation.ONGOING);
        response.setActive(Boolean.TRUE);
        return response;
    }

    private String createSequentialNumber() {
        LocalDate today = LocalDate.now();
        if (!today.equals(lastDate)) {
            sequenceByDate.clear();
            lastDate = today;
        }
        int sequence = sequenceByDate.getOrDefault(today, 0) + 1;
        sequenceByDate.put(today, sequence);
        return String.format("%s-%03d", today.toString().replace("-", ""), sequence);
    }
}
