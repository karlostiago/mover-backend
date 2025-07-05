package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.SnapshotBalanceEntity;
import com.ctsousa.mover.core.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SnapshotBalanceRepository extends JpaRepository<SnapshotBalanceEntity, Long> {

    @Query("""
            SELECT snb
            FROM SnapshotBalanceEntity snb
            WHERE snb.period BETWEEN :initialDate AND :finalDate
              AND snb.account IN :accounts
            """)
    List<SnapshotBalanceEntity> findBy(@Param("initialDate") LocalDate initialDate,
                                       @Param("finalDate") LocalDate finalDate,
                                       @Param("accounts") List<AccountEntity> accounts);
}
