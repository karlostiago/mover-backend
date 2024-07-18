package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.SenderEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Sender implements MapperToEntity<SenderEntity> {
    private Long id;

    private Long clientId;

    private String email;

    private String code;

    private LocalDateTime expiryDate;

    public void setClientId(Long clientId) {
        if (clientId == null) throw new NotificationException("");
        this.clientId = clientId;
    }

    public void setEmail(String email) {
        if (email == null) throw new NotificationException("");
        if (email.isEmpty()) throw new NotificationException("");
        if (email.equalsIgnoreCase("undefined")) throw new NotificationException("");
        this.email = email;
    }

    public void setCode(String code) {
        if (code == null) throw new NotificationException("");
        if (code.isEmpty()) throw new NotificationException("");
        if (code.equalsIgnoreCase("undefined")) throw new NotificationException("");
        this.code = code;
    }

    @Override
    public SenderEntity toEntity() {
        SenderEntity entity = new SenderEntity();
        entity.setId(this.getId());
        entity.setActive(true); // verificar
        entity.setCode(this.getCode());
        entity.setEmail(this.getEmail());
        entity.setExpiryDate(this.getExpiryDate());
        return entity;
    }
}
