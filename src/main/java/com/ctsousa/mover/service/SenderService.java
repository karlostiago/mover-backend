package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.SenderEntity;
import com.ctsousa.mover.core.service.AbstractService;

public interface SenderService extends AbstractService<SenderEntity, Long> {
    SenderEntity sendSecurityCode(Long clientId, String email);
    void sendEmailSecurityCode(String email, String code);
    SenderEntity validateSecurityCode(Long clientId, String email, String code);
}
