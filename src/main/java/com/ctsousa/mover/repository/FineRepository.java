package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.FineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FineRepository extends JpaRepository<FineEntity, Long> {

}
