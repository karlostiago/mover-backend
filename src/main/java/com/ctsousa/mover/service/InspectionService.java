package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.InspectionEntity;
import com.ctsousa.mover.core.service.BaseService;
import com.ctsousa.mover.enumeration.InspectionStatus;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface InspectionService extends BaseService<InspectionEntity, Long> {

    void startInspection(Long id, List<MultipartFile> photos ) throws IOException;

    void rejectInspection(Long inspectionId, Long photoId);

    void approveInspection(Long inspectionId, Long photoId);

    InspectionStatus getInspectionStatus(Long id);

    List<InspectionEntity> findUnderReviewInspectionsWithQuestionsByContractId(Long contractId);

    List<InspectionEntity> findInspectionById(Long id);

    List<InspectionEntity> findByContractId(Long contractId);

}
