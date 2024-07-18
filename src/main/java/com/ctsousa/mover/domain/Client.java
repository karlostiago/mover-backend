package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Client implements MapperToEntity<ClientEntity> {

    private Long id;

    private String name;

    private String cpf;

    private String email;

    private String number;

    public void setName(String name) {
        if (name == null) throw new NotificationException("");
        if (name.isEmpty()) throw new NotificationException("");
        if (name.equalsIgnoreCase("undefined")) throw new NotificationException("");
        this.name = name.toUpperCase();
    }

    public void setCpf(String cpf) {
        if (cpf == null) throw new NotificationException("");
        if (cpf.isEmpty()) throw new NotificationException("");
        if (cpf.equalsIgnoreCase("undefined")) throw new NotificationException("");
        this.cpf = cpf;
    }

    public void setEmail(String email) {
        if (email == null) throw new NotificationException("");
        if (email.isEmpty()) throw new NotificationException("");
        if (email.equalsIgnoreCase("undefined")) throw new NotificationException("");
        this.email = email.toUpperCase();
    }

    public void setNumber(String number) {
        if (number == null) throw new NotificationException("");
        if (number.isEmpty()) throw new NotificationException("");
        if (number.equalsIgnoreCase("undefined")) throw new NotificationException("");
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
