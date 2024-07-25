package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

@Getter
@Setter
public class Client implements MapperToEntity<ClientEntity> {

    private Long id;

    private String name;

    private String rg;

    private String cpf;

    private LocalDate birthDate;

    private String email;

    private String number;

    private String state;

    private String cep;

    private User user;

    public void setName(String name) {
        if (StringUtils.isBlank(name)) throw new RuntimeException("Name is blank");
        this.name = name.toUpperCase();
    }

    public void setRg(String rg) {
        if (StringUtils.isBlank(rg)) throw new RuntimeException("RG is blank");
        this.rg = rg;
    }

    public void setCpf(String cpf) {
        if (StringUtils.isBlank(cpf)) throw new RuntimeException("CPF is blank");
        this.cpf = cpf;
    }

    public void setBirthDate(LocalDate birthDate) {
        if (birthDate == null) throw new RuntimeException("Birth date is null");
        this.birthDate = birthDate;
    }

    public void setEmail(String email) {
        if (StringUtils.isBlank(email)) throw new RuntimeException("Email is blank");
        this.email = email.toUpperCase();
    }

    public void setNumber(String number) {
        if (StringUtils.isBlank(number)) throw new RuntimeException("Number is blank");
        this.number = number;
    }

    public void setState(String state) {
        if (StringUtils.isBlank(state)) throw new RuntimeException("State is blank");
        this.state = state;
    }

    public void setCep(String cep) {
        if (StringUtils.isBlank(cep)) throw new RuntimeException("CEP is blank");
        this.cep = cep;
    }

    @Override
    public ClientEntity toEntity() {
        ClientEntity entity = new ClientEntity();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setRg(this.rg);
        entity.setCpf(this.cpf);
        entity.setBirthDate(this.birthDate);
        entity.setEmail(this.email);
        entity.setNumber(this.number);
        entity.setState(this.state);
        entity.setCep(this.cep);
        if (this.user != null) {
            UserEntity userEntity = this.user.toEntity();
            entity.setUser(userEntity);
        }
        return entity;
    }
}
