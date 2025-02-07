package com.ctsousa.mover.request;

import com.ctsousa.mover.core.annotation.NotEmpty;
import com.ctsousa.mover.response.ProfileResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRequest {
    private Long id;

    @NotEmpty(message = "Nome não pode ser vázio")
    private String name;

    @NotEmpty(message = "Email não pode ser vázio")
    private String email;

    @NotEmpty(message = "Login não pode ser vázio")
    private String login;

    @NotEmpty(message = "Senha não pode ser vázio")
    private String password;

    private Long clientId;

    private List<ProfileResponse> profiles;
}
