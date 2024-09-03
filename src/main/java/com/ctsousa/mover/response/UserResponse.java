package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String login;
    private String password;
    private Long clientId;
}
