package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.ConfigurationEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.core.util.DateUtil;
import com.ctsousa.mover.enumeration.TypeValueConfiguration;
import com.ctsousa.mover.repository.ConfigurationRepository;
import com.ctsousa.mover.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ctsousa.mover.core.util.DateUtil.toDateFromBr;

@Component
public class ConfigurationServiceImpl extends BaseServiceImpl<ConfigurationEntity, Long> implements ConfigurationService {

    @Autowired
    private ConfigurationRepository repository;

    public ConfigurationServiceImpl(ConfigurationRepository repository) {
        super(repository);
    }

    @Override
    public ConfigurationEntity save(ConfigurationEntity entity) {
        if (entity.isNew()) {
            if (repository.existsByKey(entity.getKey())) {
                throw new NotificationException("Essa chave já existe.", Severity.WARNING);
            }
        } else if (!entity.isNew()) {
            if (repository.existsByKeyNotId(entity.getKey(), entity.getId())) {
                throw new NotificationException("Não foi possível atualizar, essa chave já.", Severity.WARNING);
            }
        }

        if (TypeValueConfiguration.DATE.name().equalsIgnoreCase(entity.getTypeValue())) {
            try {
                entity.setValue(toDateFromBr(entity.getValue()));
            } catch (Exception e) {
                throw new NotificationException("Valor inválido");
            }
        }

        return super.save(entity);
    }

    @Override
    public boolean verifyKeySystem(String key) {
        List<String> keys = List.of("DESVALORIZACAO_FIPE", "DESVALORIZACAO_FIPE_LEILAO");
        return keys.contains(key);
    }

    @Override
    public List<ConfigurationEntity> filterBy(String search) {
        if (search == null || search.isEmpty()) return repository.findAll();
        return repository.findBy(search);
    }
}
