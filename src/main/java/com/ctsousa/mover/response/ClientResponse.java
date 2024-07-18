package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResponse {
    private Long id;

    private String name;

    private String cpf;

    private String email;

    private String number;
}
