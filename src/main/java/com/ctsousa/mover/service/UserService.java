package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.service.BaseService;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends BaseService<UserEntity, Long>, UserDetailsService {
   UserEntity login(String cpf, String password);
}
