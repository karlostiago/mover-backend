package com.ctsousa.mover.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/***
 * Essa classe devera ser excluida e mergeada com a ClientV2Request
 */
@Getter
@Setter
@Deprecated(forRemoval = true)
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
