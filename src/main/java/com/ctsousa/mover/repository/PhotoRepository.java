package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {
}
