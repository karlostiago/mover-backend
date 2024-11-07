package com.ctsousa.mover.core.api;

import com.ctsousa.mover.enumeration.InspectionStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface InspectionApi {
    @PutMapping("/{inspectionId}/approve/{photoId}")
    ResponseEntity<String> approveInspection(@PathVariable Long inspectionId, @PathVariable Long photoId);

    @PutMapping("/{inspectionId}/reject/{photoId}")
    ResponseEntity<String> rejectInspection(@PathVariable Long inspectionId, @PathVariable Long photoId);

    @GetMapping("/{id}/status")
    ResponseEntity<InspectionStatus> getInspectionStatus(@PathVariable Long id);

    @PostMapping("/{id}/start")
    ResponseEntity<String> startInspection(@PathVariable Long id, @RequestParam("photos") List<MultipartFile> photos) throws IOException;
}
