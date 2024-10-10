package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.InspectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InspectionRepository extends JpaRepository<InspectionEntity, Long> {
    Optional<InspectionEntity> findByContractId(Long contractId);
}
