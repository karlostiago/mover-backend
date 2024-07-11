package com.ctsousa.mover.service.customServices;

import com.ctsousa.mover.core.entity.SenderEntity;

public interface CustomSenderService {
    SenderEntity sendSecurityCode(Long clientId, String email);
    void sendEmailSecurityCode(String email, String code);
    SenderEntity validateSecurityCode(Long clientId, String email, String code);
}
