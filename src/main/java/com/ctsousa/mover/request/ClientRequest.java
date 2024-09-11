package com.ctsousa.mover.request;

import com.ctsousa.mover.core.annotation.DateFormat;
import com.ctsousa.mover.core.annotation.NotEmpty;
import com.ctsousa.mover.core.deserializer.LocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ClientRequest {

    private Long id;

    @NotEmpty(message = "Nome completo não pode ser vázio")
    private String name;

    @NotEmpty(message = "RG não pode ser vázio")
    private String rg;

    @NotEmpty(message = "CPF não pode ser vázio")
    private String cpfCnpj;

    @NotEmpty(message = "Número não pode ser vázio")
    private String number;

    @NotEmpty(message = "Nome da mãe não pode ser vázio")
    private String motherName;

    @NotEmpty(message = "Estado não pode ser vázio")
    private Integer brazilianStateCode;

    @NotEmpty(message = "Bairro não pode ser vázio")
    private String neighborhood;

    @NotEmpty(message = "Cidade não pode ser vázio")
    private String city;

    private String complement;

    @NotEmpty(message = "Endereço não pode ser vázio")
    private String publicPlace;

    @NotEmpty(message = "Tipo de pessoa não pode ser vázio")
    private Integer typePersonCode;

    @DateFormat(message = "Data de nascimento inválida")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotEmpty(message = "Data de nascimento não pode ser vázio")
    private LocalDate birthDate;

    @NotEmpty(message = "Cep não pode ser vázio")
    private String postalCode;

    @NotEmpty(message = "Email não pode ser vázio")
    private String email;

    private String telephone;

    @NotEmpty(message = "Telefone celular não pode ser vázio")
    private String cellPhone;

    private Boolean active;

    private UserRequest user;

    private List<ContactRequest> contacts;
}
