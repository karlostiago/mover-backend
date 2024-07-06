package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.SenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SenderRepository extends JpaRepository<SenderEntity, Long> {
    SenderEntity findByClientIdAndEmailAndCode(Long clientId, String email, String code);
}
