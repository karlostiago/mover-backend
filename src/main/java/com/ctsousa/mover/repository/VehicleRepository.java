package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.enumeration.Situation;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {

    @NonNull
    @Override
    @Query("SELECT v FROM VehicleEntity v JOIN FETCH v.brand JOIN FETCH v.model WHERE v.id = :id")
    Optional<VehicleEntity> findById(@NonNull Long id);

    @NonNull
    @Override
    @Query("SELECT v FROM VehicleEntity v JOIN FETCH v.brand JOIN FETCH v.model WHERE 1 = 1")
    List<VehicleEntity> findAll();

    @Query("SELECT v FROM VehicleEntity v JOIN FETCH v.brand JOIN FETCH v.model WHERE v.licensePlate LIKE %:licensePlate% OR v.renavam LIKE %:renavam%")
    List<VehicleEntity> findBy(@Param("licensePlate") String licensePlate, @Param("renavam") String renavam);

    @Query("SELECT CASE WHEN COUNT(v.id) > 0 THEN TRUE ELSE FALSE END FROM VehicleEntity v WHERE v.licensePlate = :licensePlate")
    boolean existsByLicensePlate(@Param("licensePlate") String licensePlate);

    @Query("SELECT CASE WHEN COUNT(v.id) > 0 THEN TRUE ELSE FALSE END FROM VehicleEntity v WHERE v.renavam = :renavam")
    boolean existsByRenavam(@Param("renavam") String renavam);

    @Query("SELECT CASE WHEN COUNT(v.id) > 0 THEN TRUE ELSE FALSE END FROM VehicleEntity v WHERE (v.renavam = :renavam OR v.licensePlate = :licensePlate) AND v.id NOT IN (:id) ")
    boolean existsByLicensePlateOrRenavamNotId(@Param("renavam") String renavam, @Param("licensePlate") String licensePlate, @Param("id") Long id);

    @Query(value = "SELECT v.* FROM tb_vehicle v LEFT JOIN tb_contract c ON v.id = c.vehicle_id WHERE c.vehicle_id is null OR c.situation = 'CLOSED'", nativeQuery = true)
    List<VehicleEntity> onlyAvailable();

    @Query("SELECT CASE WHEN COUNT(v.id) > 0 THEN TRUE ELSE FALSE END FROM VehicleEntity v WHERE v.brand = :brand ")
    boolean brandInUse(@Param("brand") BrandEntity brand);
}
