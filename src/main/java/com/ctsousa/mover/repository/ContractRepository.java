package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.ContractEntity;
import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.enumeration.Situation;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Long> {

    boolean existsByNumber(String number);

    @NonNull
    @Override
    @Query("SELECT c FROM ContractEntity c JOIN FETCH c.client JOIN FETCH c.vehicle v JOIN FETCH v.brand JOIN FETCH v.model WHERE c.id = :id")
    Optional<ContractEntity> findById(@NonNull Long id);

    @NonNull
    @Override
    @Query("SELECT c FROM ContractEntity c JOIN FETCH c.client JOIN FETCH c.vehicle v JOIN FETCH v.brand JOIN FETCH v.model  WHERE 1 = 1")
    List<ContractEntity> findAll();

    @Query("SELECT CASE WHEN COUNT(c.id) > 0 THEN TRUE ELSE FALSE END FROM ContractEntity c JOIN c.client cc WHERE c.situation = :situation AND cc.id = :clientId")
    boolean existsClientsInProgress(@Param("situation") Situation situation, @Param("clientId") Long clientId);

//    boolean existsByHash(String hash);
//
//    @Query("SELECT CASE WHEN COUNT(c.id) > 0 THEN TRUE ELSE FALSE END FROM AccountEntity c WHERE c.number = :number AND c.name = :name AND c.id NOT IN (:id)")
//    boolean existsByNumberAndNameNotId(@Param("number") String number, @Param("name") String name, @Param("id") Long id);
//
//    @Query("SELECT ac FROM ContractEntity c WHERE ac.name LIKE %:query% OR ac.number LIKE %:query%")
//    List<ContractEntity> findBy(@Param("query") String query);
}
