package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import com.ctsousa.mover.core.validation.CpfValidator;
import com.ctsousa.mover.enumeration.BrazilianStates;
import com.ctsousa.mover.enumeration.TypePerson;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

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
    private Boolean active;
    private User user;

    @Override
    public ClientEntity toEntity() {
        if (!CpfValidator.isValid(this.getCpfCnpj())) {
            throw new NotificationException("Por gentileza informe um CPF válido.");
        }

        BrazilianStates state = BrazilianStates.toCode(brazilianStateCode);
        TypePerson typePerson = TypePerson.toCode(typePersonCode);

        ClientEntity entity = new ClientEntity();
        entity.setId(this.getId());
        entity.setName(toUppercase(this.getName()));
        entity.setRg(this.getRg());
        entity.setCpfCnpj(this.getCpfCnpj());
        entity.setNumber(this.getNumber());
        entity.setMotherName(toUppercase(this.getMotherName()));
        entity.setState(toUppercase(state.getDescription()));
        entity.setNeighborhood(toUppercase(this.getNeighborhood()));
        entity.setCity(toUppercase(this.getCity()));
        entity.setComplement(toUppercase(this.getComplement()));
        entity.setPublicPlace(toUppercase(this.getPublicPlace()));
        entity.setTypePerson(typePerson);
        entity.setBirthDate(this.getBirthDate());
        entity.setPostalCode(this.getPostalCode());
        entity.setEmail(toUppercase(this.getEmail()));
        entity.setTelephone(this.getTelephone());
        entity.setCellPhone(this.getCellPhone());
        entity.setActive(this.getActive());

        if (this.user != null) {
            UserEntity userEntity = this.user.toEntity();
            entity.setUser(userEntity);
        }

        return entity;
    }
}
