package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.InspectionApi;
import com.ctsousa.mover.service.InspectionService;
import com.ctsousa.mover.enumeration.InspectionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/inspections")
public class InspectionResource implements InspectionApi {

    private final InspectionService inspectionService;

    @Autowired
    public InspectionResource(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
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
    public ResponseEntity<String> startInspection( Long id, List<MultipartFile> photos) throws IOException {
        inspectionService.startInspection(id, photos);
        String message = "Autoinspeção iniciada com sucesso.";
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
