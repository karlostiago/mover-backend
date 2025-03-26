package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.ContractEntity;
import com.ctsousa.mover.core.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ContractService extends BaseService<ContractEntity, Long> {

    List<ContractEntity> filterBy(String search);

    Page<ContractEntity> filterBy(String search, Pageable pageable);

    ContractEntity close(ContractEntity entity);

    Optional<ContractEntity> findContratoByClientId(Long id);
}
