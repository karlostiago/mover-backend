package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import com.ctsousa.mover.core.validation.CpfValidator;
import com.ctsousa.mover.core.validation.EmailValidator;
import com.ctsousa.mover.enumeration.BrazilianStates;
import com.ctsousa.mover.enumeration.TypePerson;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private List<Contact> contacts = new ArrayList<>();

    @Override
    public ClientEntity toEntity() {
        if (!CpfValidator.isValid(this.getCpfCnpj())) {
            throw new NotificationException("CPF inválido.");
        }

        if (this.getCellPhone().length() != 11) {
            throw new NotificationException("Telefone celular inválido.");
        }

        if (this.getTelephone() != null && !this.getTelephone().isEmpty() && this.getTelephone().length() != 10) {
            throw new NotificationException("Telefone fixo inválido.");
        }

        if (this.getPostalCode().length() != 8) {
            throw new NotificationException("Cep inválido.");
        }

        EmailValidator.valid(this.getEmail());

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

        if (!contacts.isEmpty()) {
            contacts.forEach(c -> {
                c.setClient(entity);
                entity.getContacts().add(c.toEntity());
            });
        }

        if (this.user != null) {
//            entity.setUser(createUser(entity));
        }

        return entity;
    }

    private UserEntity createUser(ClientEntity entity) {
        UserEntity userEntity = this.user.toEntity();
        userEntity.setName(entity.getName());
        userEntity.setEmail(entity.getEmail());
        userEntity.setLogin(entity.getEmail());
        userEntity.setActive(this.getActive());
        return userEntity;
    }
}
