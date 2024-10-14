package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.InspectionEntity;
import com.ctsousa.mover.core.entity.InspectionPhotoEntity;
import com.ctsousa.mover.core.entity.PhotoEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.enumeration.InspectionStatus;
import com.ctsousa.mover.repository.InspectionPhotoRepository;
import com.ctsousa.mover.repository.InspectionRepository;
import com.ctsousa.mover.repository.PhotoRepository;
import com.ctsousa.mover.service.InspectionService;
import com.ctsousa.mover.service.SenderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;
import static com.ctsousa.mover.core.util.ImageUtil.savePhoto;

@Component
public class InspectionServiceImpl extends BaseServiceImpl<InspectionEntity, Long> implements InspectionService {

    @Value("${spring.mail.username}")
    private String mailMover;

    private final InspectionRepository inspectionRepository;
    private final PhotoRepository photoRepository;
    private final SenderService senderService;
    private final InspectionPhotoRepository inspectionPhotoRepository;

    public InspectionServiceImpl(InspectionRepository repository,
                                 InspectionRepository inspectionRepository,
                                 PhotoRepository photoRepository,
                                 SenderService senderService, InspectionPhotoRepository inspectionPhotoRepository) {
        super(repository);
        this.inspectionRepository = inspectionRepository;
        this.photoRepository = photoRepository;
        this.senderService = senderService;
        this.inspectionPhotoRepository = inspectionPhotoRepository;
    }

    @Override
    @Transactional
    public void startInspection(Long id, List<MultipartFile> photos) throws IOException {

        InspectionEntity inspection = inspectionRepository.findById(id)
                .orElseThrow(() -> new NotificationException("Inspeção não encontrada.", Severity.INFO));

        processInspection(photos, inspection);
        inspection.setActive(true);
        inspection.setInspectionStatus(InspectionStatus.UNDER_REVIEW);

        String emailAnalyst = mailMover;
        senderService.sendPhotosForAnalysis(emailAnalyst, inspection.getContract().getId(), inspection.getPhotos());
        inspectionRepository.save(inspection);
    }

    private void processInspection(List<MultipartFile> photos, InspectionEntity inspection) throws IOException {
        List<InspectionPhotoEntity> photoEntities = new ArrayList<>();

        for (MultipartFile photo : photos) {
            String photoUrl = savePhoto(photo);

            PhotoEntity photoEntity = new PhotoEntity();
            photoEntity.setImage(photoUrl);
            photoEntity.setActive(true);
            photoEntity = photoRepository.save(photoEntity);

            InspectionPhotoEntity inspectionPhoto = new InspectionPhotoEntity();
            inspectionPhoto.setInspectionStatus(InspectionStatus.UNDER_REVIEW);
            inspectionPhoto.setPhotoEntity(photoEntity);
            inspectionPhoto.setInspection(inspection);

            photoEntities.add(inspectionPhoto);
        }

        inspection.getPhotos().addAll(photoEntities);
    }

    private InspectionPhotoEntity findPhotoById(Long photoId) {
        return inspectionPhotoRepository.findById(photoId)
                .orElseThrow(() -> new NotificationException("Foto não encontrada.", Severity.INFO));
    }

    @Transactional
    @Override
    public void approveInspection(Long inspectionId, Long photoId) {
        InspectionEntity inspection = inspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new NotificationException("Inspeção não encontrada.", Severity.INFO));

        InspectionPhotoEntity photo = findPhotoById(photoId);
        photo.setInspectionStatus(InspectionStatus.APPROVED);

        inspection.setInspectionStatus(InspectionStatus.APPROVED);
        inspectionRepository.save(inspection);
        inspectionPhotoRepository.save(photo);

        String clientEmail = inspection.getContract().getClient().getEmail();
        senderService.sendApprovalEmail(clientEmail, inspection.getContract().getId());

    }

    @Transactional
    @Override
    public void rejectInspection(Long inspectionId, Long photoId) {
        InspectionEntity inspection = inspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new NotificationException("Inspeção não encontrada.", Severity.INFO));

        InspectionPhotoEntity photo = findPhotoById(photoId);
        photo.setInspectionStatus(InspectionStatus.REJECTED);

        inspection.setInspectionStatus(InspectionStatus.REJECTED);
        inspectionRepository.save(inspection);
        inspectionPhotoRepository.save(photo);

        String clientEmail = inspection.getContract().getClient().getEmail();
        senderService.sendRejectionEmail(clientEmail, inspection.getContract().getId(), inspection.getPhotos());

    }

    @Override
    public InspectionStatus getInspectionStatus(Long id) {
        InspectionEntity inspection = inspectionRepository.findById(id)
                .orElseThrow(() -> new NotificationException("Inspeção não encontrada.", Severity.INFO));
        return inspection.getInspectionStatus();
    }
}