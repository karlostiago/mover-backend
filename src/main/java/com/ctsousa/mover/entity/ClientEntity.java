package com.ctsousa.mover.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientEntity extends AbstractEntity {

    private String name;
    private String rg;
    private String cpf;
    private LocalDate birthDate;
    private String email;
    private String motherName;
    private String publicPlace;
    private String number;
    private String complement;
    private String district;
    private String state;
    private String cep;
    private String anexo;
}
