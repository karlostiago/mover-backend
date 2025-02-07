package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String login;
    private Long clientId;
    private Boolean active;
    private List<ProfileResponse> profiles;
}
