package com.ctsousa.mover.service;

import com.ctsousa.mover.enumeration.InspectionStatus;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface InspectionService {

    void startInspection(Long id, List<MultipartFile> photos ) throws IOException;

    void rejectInspection(Long inspectionId, Long photoId);

    void approveInspection(Long inspectionId, Long photoId);

    InspectionStatus getInspectionStatus(Long id);

}
