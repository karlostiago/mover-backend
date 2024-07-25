package com.ctsousa.mover.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientRequest {
    private Long id;

    private String name;

    private String cpf;

    private String rg;

    private String email;

    private String number;

    private String state;

    private String cep;

    private LocalDate birthDate;

    private UserRequest user;
}
