package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.InspectionEntity;
import com.ctsousa.mover.enumeration.InspectionStatus;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface InspectionService {

    void startInspection(Long id, List<MultipartFile> photos ) throws IOException;

    InspectionEntity approveInspection(Long id);

    InspectionEntity rejectInspection(Long id);

    InspectionStatus getInspectionStatus(Long id);

}
