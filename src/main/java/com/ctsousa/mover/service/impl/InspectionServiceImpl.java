package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.InspectionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.enumeration.InspectionEvents;
import com.ctsousa.mover.enumeration.InspectionStatus;
import com.ctsousa.mover.repository.InspectionRepository;
import com.ctsousa.mover.service.InspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class InspectionServiceImpl extends BaseServiceImpl<InspectionEntity, Long> implements InspectionService {

    @Autowired
    private InspectionRepository inspectionRepository;

    private StateMachineFactory<InspectionStatus, InspectionEvents> stateMachineFactory;

    public InspectionServiceImpl(InspectionRepository repository) {
        super(repository);
    }

    @Override
    public InspectionEntity getInspectionByContractId(Long contractId) {
        if (contractId == null) {
            throw new NotificationException("O ID do contrato não pode ser nulo.", Severity.ERROR);
        }

        Optional<InspectionEntity> inspectionOpt = inspectionRepository.findByContractId(contractId);
        if (inspectionOpt.isPresent()) {
            return inspectionOpt.get();
        } else {
            throw new NotificationException("Nenhuma inspeção encontrada para o contrato ID: " + contractId, Severity.WARNING);
        }
    }

    @Override
    public InspectionEntity startInspection(Long id) {
        InspectionEntity inspection = inspectionRepository.findById(id).orElseThrow(() -> new RuntimeException("Inspeção não encontrada."));
        StateMachine<InspectionStatus, InspectionEvents> stateMachine = stateMachineFactory.getStateMachine(String.valueOf(inspection.getId()));
        stateMachine.start();
        return inspection;
    }

    @Override
    public InspectionEntity approveInspection(Long id) {
        InspectionEntity inspection = inspectionRepository.findById(id).orElseThrow(() -> new RuntimeException("Inspeção não encontrada."));
        StateMachine<InspectionStatus, InspectionEvents> stateMachine = stateMachineFactory.getStateMachine(String.valueOf(inspection.getId()));
        stateMachine.sendEvent(InspectionEvents.APPROVE_INSPECTION);
        return inspection;
    }

    @Override
    public InspectionEntity rejectInspection(Long id) {
        InspectionEntity inspection = inspectionRepository.findById(id).orElseThrow(() -> new RuntimeException("Inspeção não encontrada."));
        StateMachine<InspectionStatus, InspectionEvents> stateMachine = stateMachineFactory.getStateMachine(String.valueOf(inspection.getId()));
        stateMachine.sendEvent(InspectionEvents.REJECT_INSPECTION);
        return inspection;
    }

    @Override
    public InspectionStatus getInspectionStatus(Long id) {
        InspectionEntity inspection = inspectionRepository.findById(id).orElseThrow(() -> new RuntimeException("Inspeção não encontrada."));
        StateMachine<InspectionStatus, InspectionEvents> stateMachine = stateMachineFactory.getStateMachine(String.valueOf(inspection.getId()));
        return stateMachine.getState().getId();
    }

}