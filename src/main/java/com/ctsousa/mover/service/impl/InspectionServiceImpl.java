package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.InspectionEntity;
import com.ctsousa.mover.core.entity.InspectionPhotoEntity;
import com.ctsousa.mover.core.entity.PhotoEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.enumeration.InspectionEvents;
import com.ctsousa.mover.enumeration.InspectionStatus;
import com.ctsousa.mover.repository.InspectionRepository;
import com.ctsousa.mover.repository.PhotoRepository;
import com.ctsousa.mover.service.InspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;
import static com.ctsousa.mover.core.util.ImageUtil.savePhoto;

@Component
public class InspectionServiceImpl extends BaseServiceImpl<InspectionEntity, Long> implements InspectionService {

    @Autowired
    private InspectionRepository inspectionRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private StateMachineFactory<InspectionStatus, InspectionEvents> stateMachineFactory;

    public InspectionServiceImpl(InspectionRepository repository) {
        super(repository);
    }

    @Override
    public void startInspection(Long id, List<MultipartFile> photos) throws IOException {
        InspectionEntity inspection = inspectionRepository.findById(id)
                .orElseThrow(() -> new NotificationException("Inspeção não encontrada.", Severity.INFO));

        List<InspectionPhotoEntity> photoEntities = processInspection(photos, inspection);
        inspection.setPhotos(photoEntities);

        StateMachine<InspectionStatus, InspectionEvents> stateMachine = stateMachineFactory.getStateMachine(String.valueOf(inspection.getId()));
        stateMachine.start();

        MessageHeaders headers = createHeaders(inspection);
        stateMachine.sendEvent(new GenericMessage<>(InspectionEvents.START_REVIEW, headers));

        inspectionRepository.save(inspection);
    }

    private List<InspectionPhotoEntity> processInspection(List<MultipartFile> photos, InspectionEntity inspection) throws IOException {
        List<InspectionPhotoEntity> photoEntities = new ArrayList<>();

        for (MultipartFile photo : photos) {
            String photoUrl = savePhoto(photo);
            PhotoEntity photoEntity = new PhotoEntity();
            photoEntity.setImage(photoUrl);
            photoEntity = photoRepository.save(photoEntity);

            InspectionPhotoEntity inspectionPhoto = new InspectionPhotoEntity();
            inspectionPhoto.setInspectionStatus(InspectionStatus.UNDER_REVIEW);
            inspectionPhoto.setPhotoEntity(photoEntity);
            inspectionPhoto.setInspection(inspection);

            photoEntities.add(inspectionPhoto);
        }
        return photoEntities;
    }

    private MessageHeaders createHeaders(InspectionEntity inspection) {
        Map<String, Object> headersMap = new HashMap<>();
        headersMap.put("contractId", inspection.getContract().getId());
        headersMap.put("emailAnalyst", "teste@gmail.com");
        headersMap.put("photos", inspection.getPhotos());

        return new MessageHeaders(headersMap);
    }

    @Override
    public InspectionEntity approveInspection(Long id) {
        InspectionEntity inspection = inspectionRepository.findById(id).orElseThrow(() -> new NotificationException("Inspeção não encontrada.",Severity.INFO));
        StateMachine<InspectionStatus, InspectionEvents> stateMachine = stateMachineFactory.getStateMachine(String.valueOf(inspection.getId()));
        stateMachine.sendEvent(InspectionEvents.APPROVE_INSPECTION);
        return inspection;
    }

    @Override
    public InspectionEntity rejectInspection(Long id) {
        InspectionEntity inspection = inspectionRepository.findById(id).orElseThrow(() -> new NotificationException("Inspeção não encontrada.",Severity.INFO));
        StateMachine<InspectionStatus, InspectionEvents> stateMachine = stateMachineFactory.getStateMachine(String.valueOf(inspection.getId()));
        stateMachine.sendEvent(InspectionEvents.REJECT_INSPECTION);
        return inspection;
    }

    @Override
    public InspectionStatus getInspectionStatus(Long id) {
        InspectionEntity inspection = inspectionRepository.findById(id).orElseThrow(() -> new NotificationException("Inspeção não encontrada.",Severity.INFO));
        StateMachine<InspectionStatus, InspectionEvents> stateMachine = stateMachineFactory.getStateMachine(String.valueOf(inspection.getId()));
        return stateMachine.getState().getId();
    }
}