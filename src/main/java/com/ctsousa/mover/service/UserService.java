package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.service.AbstractService;

public interface UserService extends AbstractService<UserEntity, Long> {
   UserEntity login(String cpf, String password);
}
