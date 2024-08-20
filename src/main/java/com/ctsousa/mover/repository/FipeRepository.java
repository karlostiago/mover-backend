package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.FipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FipeRepository extends JpaRepository<FipeEntity, Long> {

}
