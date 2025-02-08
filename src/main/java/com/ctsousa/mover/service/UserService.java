package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.service.BaseService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends BaseService<UserEntity, Long>, UserDetailsService {
   UserEntity login(String cpf, String password);

   List<UserEntity> filterBy(String search);
}
