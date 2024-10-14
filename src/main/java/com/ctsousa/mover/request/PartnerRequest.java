package com.ctsousa.mover.request;

import com.ctsousa.mover.core.annotation.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerRequest {

    private Long id;

    @NotEmpty(message = "Nome não pode ser vázio")
    private String name;

    @NotEmpty(message = "Email não pode ser vázio")
    private String email;

    private Boolean active;
}
