package com.ctsousa.mover.request;

import com.ctsousa.mover.core.annotation.DateFormat;
import com.ctsousa.mover.core.annotation.NotEmpty;
import com.ctsousa.mover.core.deserializer.LocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientRequest {

    private Long id;

    @NotEmpty(message = "Campo nome completo não pode ser vázio")
    private String name;

    @NotEmpty(message = "Campo rg não pode ser vázio")
    private String rg;

    @NotEmpty(message = "Campo cpf não pode ser vázio")
    private String cpfCnpj;

    @NotEmpty(message = "Campo número não pode ser vázio")
    private String number;

    @NotEmpty(message = "Campo nome da mãe não pode ser vázio")
    private String motherName;

    @NotEmpty(message = "Campo estado não pode ser vázio")
    private Integer brazilianStateCode;

    @NotEmpty(message = "Campo bairro não pode ser vázio")
    private String neighborhood;

    @NotEmpty(message = "Campo cidade não pode ser vázio")
    private String city;

    private String complement;

    @NotEmpty(message = "Campo endereço não pode ser vázio")
    private String publicPlace;

    @NotEmpty(message = "Campo tipo de pessoa não pode ser vázio")
    private Integer typePersonCode;

    @DateFormat(message = "Formato data de nascimento inválida")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotEmpty(message = "Campo data de nascimento não pode ser vázio")
    private LocalDate birthDate;

    @NotEmpty(message = "Campo cep não pode ser vázio")
    private String postalCode;

    @NotEmpty(message = "Campo email não pode ser vázio")
    private String email;

    private String telephone;

    @NotEmpty(message = "Campo telefone celular não pode ser vázio")
    private String cellPhone;

    private Boolean active;

    private UserRequest user;
}
