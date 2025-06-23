package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.repository.AccountRepository;
import com.ctsousa.mover.repository.BalanceRepository;
import com.ctsousa.mover.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Component
public class AccountServiceImpl extends BaseServiceImpl<AccountEntity, Long> implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    private final BalanceRepository balanceRepository;

    public AccountServiceImpl(AccountRepository accountRepository, BalanceRepository balanceRepository) {
        super(accountRepository);
        this.balanceRepository = balanceRepository;
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
    public void recalculateBalance() {
        List<AccountEntity> entities = findAll();
        for (AccountEntity entity : entities) {
            if (entity.getActive()) {
                BigDecimal balance = balanceRepository.accountBalance(Collections.singletonList(entity.getId()));
                entity.setAvailableBalance(balance);
                save(entity);
            }
        }
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
