package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.InspectionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.enumeration.InspectionStatus;


public interface InspectionService {

    InspectionEntity getInspectionByContractId(Long contractId) throws NotificationException;

    InspectionEntity startInspection(Long id);

    InspectionEntity approveInspection(Long id);

    InspectionEntity rejectInspection(Long id);

    InspectionStatus getInspectionStatus(Long id);

}
