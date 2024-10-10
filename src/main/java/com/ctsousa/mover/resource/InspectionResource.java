package com.ctsousa.mover.resource;

import com.ctsousa.mover.service.InspectionService;
import com.ctsousa.mover.core.entity.InspectionEntity;
import com.ctsousa.mover.enumeration.InspectionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inspections")
public class InspectionResource {

    private final InspectionService inspectionService;

    @Autowired
    public InspectionResource(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<?> startInspection(@PathVariable Long id) {
        InspectionEntity inspection = inspectionService.startInspection(id);
        return ResponseEntity.ok(inspection);
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
