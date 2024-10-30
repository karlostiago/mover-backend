package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.MaintenanceEntity;
import com.ctsousa.mover.enumeration.TypeMaintenance;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MaintenanceRepository extends JpaRepository<MaintenanceEntity, Long> {

    @NonNull
    @Override
    @Query("SELECT m FROM MaintenanceEntity m JOIN FETCH m.vehicle v JOIN FETCH v.model JOIN FETCH v.brand JOIN FETCH m.account LEFT JOIN FETCH m.card WHERE m.id = :id")
    Optional<MaintenanceEntity> findById(@NonNull Long id);

    @NonNull
    @Override
    @Query("SELECT m FROM MaintenanceEntity m JOIN FETCH m.vehicle v JOIN FETCH v.model JOIN FETCH v.brand JOIN FETCH m.account LEFT JOIN FETCH m.card WHERE 1 = 1")
    List<MaintenanceEntity> findAll();

    @Query("SELECT m FROM MaintenanceEntity m JOIN FETCH m.vehicle v JOIN FETCH v.model md JOIN FETCH v.brand b JOIN FETCH m.account LEFT JOIN FETCH m.card WHERE m.establishment LIKE %:query% OR md.name LIKE %:query% OR b.name LIKE %:query% OR v.licensePlate LIKE %:query% OR m.type LIKE %:query%")
    List<MaintenanceEntity> findBy(@Param("query") String query);

    @Query("SELECT m FROM MaintenanceEntity m JOIN FETCH m.vehicle v JOIN FETCH v.model JOIN FETCH v.brand JOIN FETCH m.account LEFT JOIN FETCH m.card WHERE m.date = :date")
    List<MaintenanceEntity> findBy(@Param("date") LocalDate date);
}
