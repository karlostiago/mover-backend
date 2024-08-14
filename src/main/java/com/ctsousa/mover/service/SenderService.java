package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.SenderEntity;
import com.ctsousa.mover.core.service.BaseService;

public interface SenderService extends BaseService<SenderEntity, Long> {
    SenderEntity sendSecurityCode(Long clientId, String email);
    void sendEmailSecurityCode(String email, String code);
    SenderEntity validateSecurityCode(Long clientId, String email, String code);
}
