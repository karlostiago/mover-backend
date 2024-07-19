package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class Client implements MapperToEntity<ClientEntity> {

    private Long id;

    private String name;

    private String cpf;

    private String email;

    private String number;

    public void setName(String name) {
        if (StringUtils.isBlank(name)) throw new RuntimeException("");
        if (StringUtils.equalsIgnoreCase(name, "undefined")) throw new RuntimeException("");
        this.name = name.toUpperCase();
    }

    public void setCpf(String cpf) {
        if (StringUtils.isBlank(cpf)) throw new RuntimeException("");
        if (StringUtils.equalsIgnoreCase(cpf, "undefined")) throw new RuntimeException("");
        this.cpf = cpf;
    }

    public void setEmail(String email) {
        if (StringUtils.isBlank(email)) throw new RuntimeException("");
        if (StringUtils.equalsIgnoreCase(email, "undefined")) throw new RuntimeException("");
        this.email = email.toUpperCase();
    }

    public void setNumber(String number) {
        if (StringUtils.isBlank(number)) throw new RuntimeException("");
        if (StringUtils.equalsIgnoreCase(number, "undefined")) throw new RuntimeException("");
        this.number = number;
    }

    @Override
    public ClientEntity toEntity() {
        ClientEntity entity = new ClientEntity();
        entity.setId(this.getId());
        entity.setCpf(this.getCpf());
        entity.setName(this.getName());
        entity.setActive(true); // verificar
        entity.setEmail(this.getEmail());
        entity.setNumber(this.getNumber());
        return entity;
    }
}
