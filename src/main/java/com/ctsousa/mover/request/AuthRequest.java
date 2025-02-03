package com.ctsousa.mover.request;

import com.ctsousa.mover.core.annotation.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {

    @NotEmpty(message = "Email não pode ser vázio ou em branco.")
    private String username;

    @NotEmpty(message = "Senha não pode ser vázio ou em branco")
    private String password;
}
