package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.service.BaseService;

import java.util.List;

public interface AccountService extends BaseService<AccountEntity, Long> {

    List<AccountEntity> filterBy(String search);

    List<AccountEntity> findByAccount(final Boolean scrowAccount);
}
