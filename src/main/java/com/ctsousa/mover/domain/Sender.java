package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.SenderEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

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
        if (StringUtils.isBlank(email)) throw new RuntimeException("");
        if (StringUtils.equalsIgnoreCase(email, "undefined")) throw new RuntimeException("");
        this.email = email;
    }

    public void setCode(String code) {
        if (StringUtils.isBlank(code)) throw new RuntimeException("");
        if (StringUtils.equalsIgnoreCase(code, "undefined")) throw new RuntimeException("");
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
