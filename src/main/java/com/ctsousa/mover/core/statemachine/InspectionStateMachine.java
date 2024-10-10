package com.ctsousa.mover.core.statemachine;

import com.ctsousa.mover.core.entity.InspectionEntity;
import com.ctsousa.mover.core.entity.InspectionPhotoEntity;
import com.ctsousa.mover.enumeration.InspectionEvents;
import com.ctsousa.mover.enumeration.InspectionStatus;
import com.ctsousa.mover.service.InspectionService;
import com.ctsousa.mover.service.SenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import java.util.List;

@Configuration
@EnableStateMachineFactory
public class InspectionStateMachine extends StateMachineConfigurerAdapter<InspectionStatus, InspectionEvents> {
    private static final Logger logger = LoggerFactory.getLogger(InspectionStateMachine.class);

    private final SenderService senderService;
    private final InspectionService inspectionService;

    public InspectionStateMachine(SenderService senderService, InspectionService inspectionService) {
        this.senderService = senderService;
        this.inspectionService = inspectionService;
    }

    @Override
    public void configure(StateMachineStateConfigurer<InspectionStatus, InspectionEvents> states) throws Exception {
        states
                .withStates()
                .initial(InspectionStatus.UNDER_REVIEW)
                .state(InspectionStatus.APPROVED)
                .state(InspectionStatus.REJECTED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<InspectionStatus, InspectionEvents> transitions) throws Exception {
        transitions
                .withExternal()
                .source(InspectionStatus.UNDER_REVIEW)
                .target(InspectionStatus.APPROVED)
                .event(InspectionEvents.APPROVE_INSPECTION)
                .action(approvedAction())

                .and()
                .withExternal()
                .source(InspectionStatus.UNDER_REVIEW)
                .target(InspectionStatus.REJECTED)
                .event(InspectionEvents.REJECT_INSPECTION)
                .action(rejectedAction())

                .and()
                .withExternal()
                .source(InspectionStatus.UNDER_REVIEW)
                .target(InspectionStatus.UNDER_REVIEW)
                .event(InspectionEvents.START_REVIEW)
                .action(underReviewAction());
    }

    @Bean
    public StateMachineListenerAdapter<InspectionStatus, InspectionEvents> stateMachineListener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<InspectionStatus, InspectionEvents> from, State<InspectionStatus, InspectionEvents> to) {
                if (from != null) {
                    logger.info("Status da máquina de inspeção alterada de {} para {}", from.getId(), to.getId());
                } else {
                    logger.info("Iniciando máquina de inspeção com id: {}", to.getId());
                }
            }
        };
    }


    @Bean
    public Action<InspectionStatus, InspectionEvents> approvedAction() {
        return context -> {
            logger.info("A inspeção foi aprovada.");
            Long contractId = (Long) context.getMessageHeader("contractId");

        };
    }

    @Bean
    public Action<InspectionStatus, InspectionEvents> rejectedAction() {
        return context -> {
            logger.info("A inspeção foi rejeitada.");
            Long contractId = (Long) context.getMessageHeader("contractId");

        };
    }

    @Bean
    public Action<InspectionStatus, InspectionEvents> underReviewAction() {
        return context -> {
            logger.info("A inspeção está em análise. Enviando fotos para o e-mail do analista.");
            Long contractId = (Long) context.getMessageHeader("contractId");
            String emailAnalyst = (String) context.getMessageHeader("emailAnalyst");

            InspectionEntity inspection = inspectionService.getInspectionByContractId(contractId);
            if (inspection != null) {
                List<InspectionPhotoEntity> photos = inspection.getPhotos();
                senderService.sendPhotosForAnalysis(emailAnalyst, contractId, photos);
            } else {
                logger.warn("Nenhuma inspeção encontrada para o contrato ID: {}", contractId);
            }
        };
    }
}
