package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.enumeration.Functionality;
import com.ctsousa.mover.repository.PermissionRepository;
import com.ctsousa.mover.repository.UserRepository;
import com.ctsousa.mover.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class CustomUserDetailServiceImpl implements CustomUserDetailService {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    private static final String PREFIX = "ROLE_";

    @Value("${mover.root-user}")
    private String rootUser;

    @Value("${mover.root-user-password}")
    private String rootUserPassword;

    private UserEntity user;

    public CustomUserDetailServiceImpl(UserRepository userRepository, PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (rootUser.equalsIgnoreCase(username)) {
            return createRootUser();
        }
        return loadUserFromDatabase(username);
    }

    @Override
    public List<String> getPermissions(final String username) {
        if (rootUser.equalsIgnoreCase(username)) {
            return Stream.of(Functionality.values())
                    .map(f -> PREFIX + f.name())
                    .toList();
        }

        return permissionRepository.findByUser(
                        userRepository.findByLogin(username)
                                .orElseThrow(() -> new NotificationException("Usuário não encontrado."))
                                .getId())
                .stream()
                .map(p -> PREFIX + p)
                .toList();
    }

    @Override
    public UserEntity getUser() {
        return user;
    }

    private UserDetails createRootUser() {
        this.user = new UserEntity(rootUser);
        return createUserDetails(rootUser, rootUserPassword, getPermissions(rootUser));
    }

    private UserDetails loadUserFromDatabase(String username) {
        UserEntity entity = userRepository.findByLogin(username)
                .orElseThrow(() -> new NotificationException("Usuário não encontrado."));

        if (!entity.getActive()) {
            throw new NotificationException("Usuário não está ativo.");
        }

        this.user = entity;

        return createUserDetails(entity.getLogin(), entity.getPassword(), getPermissions(username));
    }

    private UserDetails createUserDetails(String username, String password, List<String> permissions) {
        return User.withUsername(username)
                .password(password)
                .authorities(permissions.toArray(new String[0]))
                .build();
    }
}
