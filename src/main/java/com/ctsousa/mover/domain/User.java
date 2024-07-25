package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class User implements MapperToEntity<UserEntity> {

    private Long id;
    private String name;
    private String email;
    private String login;
    private String password;
    private Long clientId;

    public void setName(String name) {
        if (StringUtils.isBlank(name)) throw new RuntimeException("Name cannot be blank");
        if (StringUtils.equalsIgnoreCase(name, "undefined")) throw new RuntimeException("Name cannot be 'undefined'");
        this.name = name.toUpperCase();
    }

    public void setEmail(String email) {
        if (StringUtils.isBlank(email)) throw new RuntimeException("Email cannot be blank");
        if (StringUtils.equalsIgnoreCase(email, "undefined")) throw new RuntimeException("Email cannot be 'undefined'");
        this.email = email.toUpperCase();
    }

    public void setLogin(String login) {
        if (StringUtils.isBlank(login)) throw new RuntimeException("Login cannot be blank");
        if (StringUtils.equalsIgnoreCase(login, "undefined")) throw new RuntimeException("Login cannot be 'undefined'");
        this.login = login;
    }

    public void setPassword(String password) {
        if (StringUtils.isBlank(password)) throw new RuntimeException("Password cannot be blank");
        if (StringUtils.equalsIgnoreCase(password, "undefined")) throw new RuntimeException("Password cannot be 'undefined'");
        this.password = password;
    }

    @Override
    public UserEntity toEntity() {
        UserEntity entity = new UserEntity();
        entity.setId(this.getId());
        entity.setName(this.getName());
        entity.setEmail(this.getEmail());
        entity.setLogin(this.getLogin());
        entity.setPassword(this.getPassword());
        entity.setClientId(this.getClientId());
        return entity;
    }
}
