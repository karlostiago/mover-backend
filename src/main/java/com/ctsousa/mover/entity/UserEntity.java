package com.ctsousa.mover.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends AbstractEntity {

    private String name;
    private String email;
    private String login;
    private String password;
}
