package com.ctsousa.mover.request;

import com.ctsousa.mover.core.annotation.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigurationRequest {
    private Long id;

    @NotEmpty(message = "Chave não pode ser vázio")
    private String key;

    @NotEmpty(message = "Valor não pode ser vázio")
    private String value;

    @NotEmpty(message = "Tipo não pode ser vázio")
    private String typeValue;

    private Boolean active;
}
