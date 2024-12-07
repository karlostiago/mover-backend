package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    boolean existsByHash(String hash);

    @Query("SELECT CASE WHEN COUNT(c.id) > 0 THEN TRUE ELSE FALSE END FROM AccountEntity c WHERE c.number = :number AND c.name = :name AND c.id NOT IN (:id)")
    boolean existsByNumberAndNameNotId(@Param("number") String number, @Param("name") String name, @Param("id") Long id);

    @Query("SELECT ac FROM AccountEntity ac WHERE ac.name LIKE %:query% OR ac.number LIKE %:query%")
    List<AccountEntity> findBy(@Param("query") String query);

    @Query("SELECT c FROM AccountEntity c WHERE c.caution = :scrowAccount")
    List<AccountEntity> findByAccount(@Param("scrowAccount") Boolean scrowAccount);
}
