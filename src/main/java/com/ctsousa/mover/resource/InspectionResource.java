package com.ctsousa.mover.resource;

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
public class InspectionResource {

    private final InspectionService inspectionService;

    @Autowired
    public InspectionResource(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    @PutMapping("/{inspectionId}/approve/{photoId}")
    public ResponseEntity<String> approveInspection(@PathVariable Long inspectionId, @PathVariable Long photoId) {
        inspectionService.approveInspection(inspectionId, photoId);
        return ResponseEntity.ok("Inspeção aprovada com sucesso.");
    }

    @PutMapping("/{inspectionId}/reject/{photoId}")
    public ResponseEntity<String> rejectInspection(@PathVariable Long inspectionId, @PathVariable Long photoId) {
        inspectionService.rejectInspection(inspectionId, photoId);
        return ResponseEntity.ok("Inspeção com ID " + inspectionId + " rejeitada pelo analista.");
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<InspectionStatus> getInspectionStatus(@PathVariable Long id) {
        InspectionStatus status = inspectionService.getInspectionStatus(id);
        return ResponseEntity.ok(status);
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<String> startInspection(@PathVariable Long id,
                                                  @RequestParam("photos") List<MultipartFile> photos) throws IOException {
        inspectionService.startInspection(id, photos);
        String message = "Autoinspeção iniciada com sucesso.";
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
