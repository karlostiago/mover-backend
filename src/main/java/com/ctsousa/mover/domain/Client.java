package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.annotation.NotEmpty;
import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Client implements MapperToEntity<ClientEntity> {
    private Long id;
    private String name;
    private String rg;
    private String cpfCnpj;
    private String number;
    private String motherName;
    private Integer brazilianStateCode;
    private String neighborhood;
    private String city;
    private String complement;
    private String publicPlace;
    private Integer typePersonCode;
    private LocalDate birthDate;
    private String postalCode;
    private String email;
    private String telephone;
    private String cellPhone;
    private User user;

    @Override
    public ClientEntity toEntity() {
        ClientEntity entity = new ClientEntity();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setRg(this.rg);
        entity.setCpfCnpj(this.getCpfCnpj());
        entity.setBirthDate(this.birthDate);
        entity.setEmail(this.email);
        entity.setNumber(this.number);
//        entity.setState(this.state);
//        entity.setCep(this.cep);
        if (this.user != null) {
            UserEntity userEntity = this.user.toEntity();
            entity.setUser(userEntity);
        }
        return entity;
    }
}
