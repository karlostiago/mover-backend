package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.ParameterEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.enumeration.TypeValueParameter;
import com.ctsousa.mover.repository.ParameterRepository;
import com.ctsousa.mover.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ctsousa.mover.core.util.DateUtil.toDateFromBr;

@Component
public class ParameterServiceImpl extends BaseServiceImpl<ParameterEntity, Long> implements ParameterService {

    @Autowired
    private ParameterRepository repository;

    public ParameterServiceImpl(ParameterRepository repository) {
        super(repository);
    }

    @Override
    public ParameterEntity save(ParameterEntity entity) {
        if (entity.isNew()) {
            if (repository.existsByKey(entity.getKey())) {
                throw new NotificationException("Essa chave já existe.", Severity.WARNING);
            }
        } else if (!entity.isNew()) {
            if (repository.existsByKeyNotId(entity.getKey(), entity.getId())) {
                throw new NotificationException("Não foi possível atualizar, essa chave já.", Severity.WARNING);
            }
        }

        if (TypeValueParameter.DATE.name().equalsIgnoreCase(entity.getTypeValue())) {
            try {
                entity.setValue(toDateFromBr(entity.getValue()));
            } catch (Exception e) {
                throw new NotificationException("Valor inválido");
            }
        }

        return super.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new NotificationException("Essa Parametrização é essencial para o sistema e não pode ser removida.");
        }
    }

    @Override
    public boolean verifyKeySystem(String key) {
        List<String> keys = List.of("DESVALORIZACAO_FIPE", "DESVALORIZACAO_FIPE_LEILAO");
        return keys.contains(key);
    }

    @Override
    public List<ParameterEntity> filterBy(String search) {
        if (search == null || search.isEmpty()) return repository.findAll();
        return repository.findBy(search);
    }
}
