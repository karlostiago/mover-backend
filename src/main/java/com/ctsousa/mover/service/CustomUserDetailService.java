package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface CustomUserDetailService extends UserDetailsService {
    List<String> getPermissions(final String username);

    UserEntity getUser();
}
