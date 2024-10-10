package com.ctsousa.mover.resource;

import com.ctsousa.mover.service.InspectionService;
import com.ctsousa.mover.core.entity.InspectionEntity;
import com.ctsousa.mover.enumeration.InspectionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/inspections")
public class InspectionResource {

    private final InspectionService inspectionService;

    @Autowired
    public InspectionResource(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<String> startInspection(@PathVariable Long id, @RequestParam("photos") List<MultipartFile> photos) {
        try {
            inspectionService.startInspection(id, photos);
            return ResponseEntity.ok("Inspeção efetuada com sucesso e fotos enviadas para o analista da Mover Frota.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não conseguimos iniciar a auto inspeção.");
        }
    }
    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveInspection(@PathVariable Long id) {
        InspectionEntity inspection = inspectionService.approveInspection(id);
        return ResponseEntity.ok(inspection);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectInspection(@PathVariable Long id) {
        InspectionEntity inspection = inspectionService.rejectInspection(id);
        return ResponseEntity.ok(inspection);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<?> getInspectionStatus(@PathVariable Long id) {
        InspectionStatus status = inspectionService.getInspectionStatus(id);
        return ResponseEntity.ok(status);
    }
}
