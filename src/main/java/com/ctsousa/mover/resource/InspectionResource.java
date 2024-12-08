package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.InspectionApi;
import com.ctsousa.mover.core.api.resource.SimpleBaseResource;
import com.ctsousa.mover.core.entity.InspectionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.service.BaseService;
import com.ctsousa.mover.enumeration.InspectionStatus;
import com.ctsousa.mover.request.InspectionRequest;
import com.ctsousa.mover.response.InspectionResponse;
import com.ctsousa.mover.service.InspectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/inspections")
public class InspectionResource extends SimpleBaseResource<InspectionResponse, InspectionRequest, InspectionEntity> implements InspectionApi {

    private final InspectionService inspectionService;

    public InspectionResource(BaseService<InspectionEntity, Long> service, InspectionService inspectionService) {
        super(service);
        this.inspectionService = inspectionService;
    }

    @Override
    public ResponseEntity<InspectionResponse> startInspection(Long id, List<MultipartFile> photos) throws IOException {

        inspectionService.startInspection(id, photos);

        List<InspectionEntity> inspections = inspectionService.findInspectionById(id);

        if (inspections.isEmpty()) {
            throw new NotificationException("Nenhuma inspeção encontrada para o ID do contrato fornecido.");
        }

        InspectionEntity selectedInspection = inspections.stream()
                .max(Comparator.comparing(InspectionEntity::getDate))
                .orElseThrow(() -> new NotificationException("Não foi possível determinar a última inspeção."));

        InspectionResponse response = toResponse(selectedInspection);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public List <InspectionEntity>findUnderReviewInspectionsWithQuestionsByContractId(Long contractId) {
        List<InspectionEntity> inspections = inspectionService.findUnderReviewInspectionsWithQuestionsByContractId(contractId);
        if (inspections.isEmpty()) {
            throw new NotificationException("Nenhuma inspeção encontrada para o ID do contrato fornecido.");
        }
        return inspections;
    }

    @Override
    public List<InspectionEntity> getInspectionsByContractId(Long contractId) {
        List<InspectionEntity> inspections = inspectionService.findByContractId(contractId);
        if (inspections.isEmpty()) {
            throw new NotificationException("Nenhuma inspeção encontrada para o ID do contrato fornecido.");
        }
        return inspections;
    }

    @Override
    public ResponseEntity<String> approveInspection(Long inspectionId, Long photoId) {
        inspectionService.approveInspection(inspectionId, photoId);
        return ResponseEntity.ok("Inspeção aprovada com sucesso.");
    }

    @Override
    public ResponseEntity<String> rejectInspection(Long inspectionId, Long photoId) {
        inspectionService.rejectInspection(inspectionId, photoId);
        return ResponseEntity.ok("Inspeção com ID " + inspectionId + " rejeitada pelo analista.");
    }

    @Override
    public ResponseEntity<InspectionStatus> getInspectionStatus(Long id) {
        InspectionStatus status = inspectionService.getInspectionStatus(id);
        return ResponseEntity.ok(status);
    }

    @Override
    protected Class<InspectionResponse> responseClass() {
        return InspectionResponse.class;
    }

    @Override
    protected Class<InspectionEntity> entityClass() {
        return InspectionEntity.class;
    }
}
