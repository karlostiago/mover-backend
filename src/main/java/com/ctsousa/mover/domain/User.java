package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.ProfileEntity;
import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.mapper.MapperToEntity;
import com.ctsousa.mover.core.validation.EmailValidator;
import com.ctsousa.mover.core.validation.NotEmptyValidator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Getter
@Setter
public class User implements MapperToEntity<UserEntity> {

    private Long id;
    private String name;
    private String email;
    private String login;
    private String password;
    private Long clientId;
    private List<Profile> profiles;
    private Boolean active;

    @Override
    public UserEntity toEntity() {

        EmailValidator.valid(this.getEmail());

        UserEntity entity = new UserEntity();
        entity.setId(this.getId());

        if (entity.isNew() && !NotEmptyValidator.isValid(this.password)) {
            throw new NotificationException("Senha não pode ser vázio");
        }

        entity.setName(toUppercase(this.getName()));
        entity.setEmail(toUppercase(this.getEmail()));
        entity.setLogin(this.getLogin());

        if (NotEmptyValidator.isValid(this.password)) {
            if (this.password.length() < 6) throw new NotificationException("A senha não pode ter menos de 6 caracteres.");

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            entity.setPassword(encoder.encode(this.getPassword()));
        }

        entity.setClientId(this.getClientId());
        entity.setProfiles(getProfiles());
        entity.setActive(this.getActive());

        return entity;
    }

    private Set<ProfileEntity> getProfiles() {
        return profiles.stream()
                .map(p -> new ProfileEntity(p.getId()))
                .collect(Collectors.toSet());
    }
}
