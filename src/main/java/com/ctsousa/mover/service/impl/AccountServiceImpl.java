package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.repository.AccountRepository;
import com.ctsousa.mover.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountServiceImpl extends BaseServiceImpl<AccountEntity, Long> implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        super(accountRepository);
    }

    @Override
    public AccountEntity save(AccountEntity entity) {
        if (entity.isNew()) {
            if (accountRepository.existsByHash(entity.getHash())) {
                throw new NotificationException("Já existe uma conta cadastrada, com os dados informados.", Severity.WARNING);
            }
        } else if (!entity.isNew()) {
            if (accountRepository.existsByNumberAndNameNotId(entity.getNumber(), entity.getName(), entity.getId())) {
                throw new NotificationException("Não foi possível atualizar, já tem uma conta, com os dados informado.", Severity.WARNING);
            }
        }
        return super.save(entity);
    }

    @Override
    public List<AccountEntity> filterBy(String search) {
        if (search == null || search.isEmpty()) return accountRepository.findAll();
        return accountRepository.findBy(search);
    }

    @Override
    public void deleteById(Long id) {
        try {
            super.deleteById(id);
        } catch (Exception e) {
            throw new NotificationException("Essa conta já esta em uso e não pode ser excluída.", Severity.ERROR);
        }
    }
}
