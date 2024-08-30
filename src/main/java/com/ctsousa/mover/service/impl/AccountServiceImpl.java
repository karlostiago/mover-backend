package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.repository.AccountRepository;
import com.ctsousa.mover.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountServiceImpl extends BaseServiceImpl<AccountEntity, Long> implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository repository) {
        super(repository);
    }

    @Override
    public AccountEntity save(AccountEntity entity) {
        if (entity.isNew()) {
            if (accountRepository.existsByHash(entity.getHash())) {
                throw new NotificationException("Já existe uma conta cadastrada, com os dados informados.", Severity.WARNING);
            }
        } else if (!entity.isNew()) {
//            if (brandRepository.existsByNameNotId(entity.getName(), entity.getId())) {
//                throw new NotificationException("Não foi possível atualizar, pois já tem uma marca, com o nome informado.", Severity.WARNING);
//            }
        }
        return super.save(entity);
    }
}
