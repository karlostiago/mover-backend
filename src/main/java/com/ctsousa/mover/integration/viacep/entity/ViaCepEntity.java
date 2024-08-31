package com.ctsousa.mover.integration.viacep.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViaCepEntity {
    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
    private boolean erro;
}
