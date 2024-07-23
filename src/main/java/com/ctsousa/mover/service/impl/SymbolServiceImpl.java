package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.SymbolEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.service.impl.AbstractServiceImpl;
import com.ctsousa.mover.repository.SymbolRepository;
import com.ctsousa.mover.service.SymbolService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class SymbolServiceImpl extends AbstractServiceImpl<SymbolEntity, Long> implements SymbolService {

    private final SymbolRepository symbolRepository;

    public SymbolServiceImpl(JpaRepository<SymbolEntity, Long> repository) {
        super(repository);
        this.symbolRepository = (SymbolRepository) repository;
    }

    @Override
    public SymbolEntity save(SymbolEntity entity) {
        if (this.symbolRepository.existsByDescription(entity.getDescription())) {
            throw new NotificationException("Já existe um simbolo com o nome informado.");
        }
        return super.save(entity);
    }

    @Override
    public SymbolEntity findByDescription(String description) {
        SymbolEntity entity = this.symbolRepository.findByDescription(description.toUpperCase());

        if (entity == null) {
            throw new NotificationException("Não foi encontrado um simbolo com a descrição informada.");
        }

        return entity;
    }
}
