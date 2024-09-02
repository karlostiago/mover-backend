package com.ctsousa.mover.response;

import com.ctsousa.mover.enumeration.BrazilianStates;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientResponse {
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    private String postalCode;
    private String email;
    private String telephone;
    private String cellPhone;
    private String state;
    private String typePerson;
    private Boolean active;
    private String uf;
    private UserResponse user;

    public void setState(String brazilianStateName) {
        BrazilianStates state = BrazilianStates.toName(brazilianStateName);
        this.brazilianStateCode = state.getCode();
        this.uf = state.name();
    }
}
