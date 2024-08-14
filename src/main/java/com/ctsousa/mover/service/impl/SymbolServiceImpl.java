package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.SymbolEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.repository.SymbolRepository;
import com.ctsousa.mover.service.SymbolService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SymbolServiceImpl extends BaseServiceImpl<SymbolEntity, Long> implements SymbolService {

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
    public void deleteById(Long id) {
        boolean removeSymbol = true;

        SymbolEntity symbol = findById(id);

        for (String s : loadPredefinedSymbols()) {
            if (s.equalsIgnoreCase(symbol.getDescription())) {
                removeSymbol = false;
                break;
            }
        }

        if (removeSymbol) {
            symbolRepository.deleteById(id);
        }
    }

    private List<String> loadPredefinedSymbols() {
        return List.of("VOLKSWAGEN", "CHEVROLET", "AUDI", "FORD",
            "RENAULT", "HYUNDAI", "TOYOTA", "HONDA", "NISSAN", "JEEP", "JAGUAR",
            "LAND_ROVER", "LAND ROVER", "LANDROVER", "LAND", "ROVER", "VOLVO", "PORSCHE",
            "LEXUS", "KIA", "CITROEN", "PEUGEOT", "MITSUBISHI", "SUZUKI", "MINI",
            "SUBARU", "RAM", "DODGE", "GMC", "TESLA", "MERCEDES", "BMW", "FIAT"
        );
    }
}
