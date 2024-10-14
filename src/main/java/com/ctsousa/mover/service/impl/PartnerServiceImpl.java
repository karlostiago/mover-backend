package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.PartnerEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.repository.PartnerRepository;
import com.ctsousa.mover.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Component
public class PartnerServiceImpl extends BaseServiceImpl<PartnerEntity, Long> implements PartnerService {

    @Autowired
    private PartnerRepository repository;

    public PartnerServiceImpl(PartnerRepository repository) {
        super(repository);
    }

    @Override
    public PartnerEntity save(PartnerEntity entity) {
        if (entity.isNew()) {
            if (repository.existsByEmail(entity.getEmail())) {
                throw new NotificationException("Já existe um sócio com o e-mail cadastrado.", Severity.WARNING);
            }
        } else if (!entity.isNew()) {
            if (repository.existsByEmailNotId(entity.getEmail(), entity.getId())) {
                throw new NotificationException("Não foi possível atualizar, já existe um sócio com os dados informados.", Severity.WARNING);
            }
        }

        return super.save(entity);
    }

    @Override
    public List<PartnerEntity> filterBy(String search) {
        if (search == null || search.isEmpty()) return repository.findAll();
        return repository.findBy(toUppercase(search));
    }
}
