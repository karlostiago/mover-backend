package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.ContractEntity;
import com.ctsousa.mover.core.entity.InspectionEntity;
import com.ctsousa.mover.core.entity.InspectionPhotoEntity;
import com.ctsousa.mover.core.entity.PhotoEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.enumeration.InspectionStatus;
import com.ctsousa.mover.repository.ContractRepository;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class InspectionServiceImpl extends BaseServiceImpl<InspectionEntity, Long> implements InspectionService {

    @Value("${spring.mail.username}")
    private String mailMover;

    private final InspectionRepository inspectionRepository;
    private final PhotoRepository photoRepository;
    private final SenderService senderService;
    private final InspectionPhotoRepository inspectionPhotoRepository;
    private final ContractRepository contractRepository;

    public InspectionServiceImpl(InspectionRepository repository,
                                 InspectionRepository inspectionRepository,
                                 PhotoRepository photoRepository,
                                 SenderService senderService, InspectionPhotoRepository inspectionPhotoRepository, ContractRepository contractRepository) {
        super(repository);
        this.inspectionRepository = inspectionRepository;
        this.photoRepository = photoRepository;
        this.senderService = senderService;
        this.inspectionPhotoRepository = inspectionPhotoRepository;
        this.contractRepository = contractRepository;
    }

    @Override
    @Transactional
    public void startInspection(Long id, List<MultipartFile> photos) throws IOException {

        // Criar uma nova inspeção
        InspectionEntity newInspection = new InspectionEntity();

        // Encontrar o contrato associado ao id
        ContractEntity contract = contractRepository.findById(id)
                .orElseThrow(() -> new NotificationException("Contrato não encontrado."));

        // Configurar os detalhes da nova inspeção
        newInspection.setContract(contract);
        newInspection.setActive(true);
        newInspection.setInspectionStatus(InspectionStatus.UNDER_REVIEW);
        newInspection.setDate(LocalDate.from(LocalDateTime.now())); // ou qualquer outra lógica para data

        // Salvar a nova inspeção
        inspectionRepository.save(newInspection);

        // Processar as fotos
        List<InspectionPhotoEntity> photoList = new ArrayList<>();
        for (MultipartFile photo : photos) {
            PhotoEntity photoEntity = savePhoto(photo); // Método para salvar a foto

            InspectionPhotoEntity inspectionPhoto = new InspectionPhotoEntity();
            inspectionPhoto.setInspection(newInspection); // Associar a foto à nova inspeção
            inspectionPhoto.setInspectionStatus(InspectionStatus.UNDER_REVIEW);
            inspectionPhoto.setPhotoEntity(photoEntity);

            photoList.add(inspectionPhoto);
        }

        // Salvar as fotos associadas à nova inspeção
        inspectionPhotoRepository.saveAll(photoList);

        // Enviar email com as fotos para análise
        String emailAnalyst = this.mailMover;
        senderService.sendPhotosForAnalysis(emailAnalyst, contract.getId(), photoList);
    }


    @Override
    public  List<InspectionEntity> findInspectionById(Long id) {
        List<InspectionEntity> inspections = inspectionRepository.findByContractId(id);
        if (inspections.isEmpty()) {
            throw new RuntimeException("Nenhuma inspeção encontrada para o contrato com ID: " + id);
        }
        return inspections;
    }

    @Override
    public List<InspectionEntity> findByContractId(Long contractId) {
        return inspectionRepository.findAllByContractId(contractId);
    }

    private void processInspection(List<MultipartFile> photos, InspectionEntity inspection) throws IOException {
        Set<InspectionPhotoEntity> photoEntities = new HashSet<>();

        for (MultipartFile photo : photos) {
            PhotoEntity photoEntity = savePhoto(photo);
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

        List<InspectionPhotoEntity> photoList = new ArrayList<>(inspection.getPhotos());
        String clientEmail = inspection.getContract().getClient().getEmail();

        senderService.sendRejectionEmail(clientEmail, inspection.getContract().getId(), photoList);

    }

    @Override
    public InspectionStatus getInspectionStatus(Long id) {
        InspectionEntity inspection = inspectionRepository.findById(id)
                .orElseThrow(() -> new NotificationException("Inspeção não encontrada.", Severity.INFO));
        return inspection.getInspectionStatus();
    }

    @Override
    public List<InspectionEntity> findUnderReviewInspectionsWithQuestionsByContractId(Long contractId) {
        return inspectionRepository.findUnderReviewInspectionsWithQuestionsByContractId(contractId);
    }

    public PhotoEntity savePhoto(MultipartFile photo) throws IOException {
        String base64Image = Base64.getEncoder().encodeToString(photo.getBytes());
        PhotoEntity newPhoto = new PhotoEntity();
        newPhoto.setImage("data:image/jpeg;base64," + base64Image);

        return photoRepository.save(newPhoto);
    }
}