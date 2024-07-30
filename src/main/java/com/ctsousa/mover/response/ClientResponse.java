package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResponse {
    private Long id;

    private String name;

    private String rg;

    private String state;

    private String cep;

    private String cpf;

    private String email;

    private String number;

    private UserResponse user;
}
