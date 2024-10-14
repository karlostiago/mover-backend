package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.PartnerEntity;
import com.ctsousa.mover.core.validation.EmailValidator;
import lombok.Getter;
import lombok.Setter;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Getter
@Setter
public class Partner extends DomainModel<PartnerEntity> {

    private String name;
    private String email;

    @Override
    public PartnerEntity toEntity() {
        EmailValidator.valid(this.getEmail());
        PartnerEntity entity = new PartnerEntity();
        entity.setId(this.getId());
        entity.setName(toUppercase(this.getName()));
        entity.setEmail(toUppercase(this.getEmail()));
        entity.setActive(this.getActive());
        return entity;
    }
}
