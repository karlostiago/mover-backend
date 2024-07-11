package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.SenderEntity;
import com.ctsousa.mover.core.service.AbstractService;
import com.ctsousa.mover.service.customServices.CustomSenderService;

public interface SenderService extends AbstractService<SenderEntity, Long>, CustomSenderService {
    SenderEntity sendSecurityCode(Long clientId, String email);
    void sendEmailSecurityCode(String email, String code);
    SenderEntity validateSecurityCode(Long clientId, String email, String code);
}
