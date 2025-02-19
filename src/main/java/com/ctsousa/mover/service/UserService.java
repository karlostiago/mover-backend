package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.service.BaseService;

import java.util.List;

public interface UserService extends BaseService<UserEntity, Long> {
   UserEntity login(String cpf, String password);

   List<UserEntity> filterBy(String search);

   UserEntity getUser();

   void changePassword(final UserEntity entity);
}
