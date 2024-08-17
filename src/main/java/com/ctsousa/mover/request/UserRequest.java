package com.ctsousa.mover.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
        private Long id;
        private String name;
        private String email;
        private String login;
        private String password;
        private Long clientId;
}
