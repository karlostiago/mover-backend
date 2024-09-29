package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.CardEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.repository.AccountRepository;
import com.ctsousa.mover.repository.CardRepository;
import com.ctsousa.mover.service.AccountService;
import com.ctsousa.mover.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CardServiceImpl extends BaseServiceImpl<CardEntity, Long> implements CardService {

    @Autowired
    private CardRepository repository;

    public CardServiceImpl(CardRepository repository) {
        super(repository);
    }

    @Override
    public CardEntity save(CardEntity entity) {
//        if (entity.isNew()) {
//            if (repository.existsByHash(entity.getHash())) {
//                throw new NotificationException("Já existe uma conta cadastrada, com os dados informados.", Severity.WARNING);
//            }
//        } else if (!entity.isNew()) {
//            if (repository.existsByNumberAndNameNotId(entity.getNumber(), entity.getName(), entity.getId())) {
//                throw new NotificationException("Não foi possível atualizar, já tem uma conta, com os dados informado.", Severity.WARNING);
//            }
//        }
        return super.save(entity);
    }

    @Override
    public List<CardEntity> filterBy(String search) {
        if (search == null || search.isEmpty()) return repository.findAll();
        return repository.findBy(search);
    }
}
