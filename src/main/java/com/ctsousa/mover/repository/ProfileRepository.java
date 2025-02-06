package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.ProfileEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    ProfileEntity findByDescription(String description);

    @NonNull
    @Override
    @Query("SELECT p FROM ProfileEntity p JOIN FETCH p.permissions WHERE p.id = :id")
    Optional<ProfileEntity> findById(@NonNull Long id);
}
