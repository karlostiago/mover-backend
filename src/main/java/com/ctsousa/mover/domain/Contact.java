package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.entity.ContactEntity;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import lombok.Getter;
import lombok.Setter;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Getter
@Setter
public class Contact implements MapperToEntity<ContactEntity> {
    private Long id;
    private String name;
    private String telephone;
    private String degreeKinship;
    private ClientEntity client;

    @Override
    public ContactEntity toEntity() {
        ContactEntity entity = new ContactEntity();
        entity.setId(this.getId());
        entity.setName(toUppercase(this.getName()));
        entity.setTelephone(this.getTelephone());
        entity.setClient(this.getClient());
        entity.setDegreeKinship(toUppercase(this.getDegreeKinship()));
        entity.setActive(this.getClient().getActive());
        return entity;
    }
}
