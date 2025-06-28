package com.ctsousa.mover.integration.viacep.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViaCep {
    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
    private boolean erro;
}
