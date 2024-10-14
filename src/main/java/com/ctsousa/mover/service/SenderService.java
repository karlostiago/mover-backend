package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.InspectionPhotoEntity;
import com.ctsousa.mover.core.entity.SenderEntity;
import com.ctsousa.mover.core.service.BaseService;
import java.util.List;

public interface SenderService extends BaseService<SenderEntity, Long> {
    SenderEntity sendSecurityCode(Long clientId, String email);
    void sendEmailSecurityCode(String email, String code);
    SenderEntity validateSecurityCode(Long clientId, String email, String code);
    void sendPhotosForAnalysis(String emailAnalyst, Long contractId, List<InspectionPhotoEntity> photos);
    void sendApprovalEmail(String clientEmail, Long contractId);
    void sendRejectionEmail(String clientEmail, Long contractId, List<InspectionPhotoEntity> photos);
}
