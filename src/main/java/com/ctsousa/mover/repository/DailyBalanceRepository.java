package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.DailyBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyBalanceRepository extends JpaRepository<DailyBalanceEntity, Long> {

    @Query("""
            SELECT d
            FROM DailyBalanceEntity d
            WHERE d.period BETWEEN :periodInitial AND :periodFinal
              AND d.account IN (:accounts)
            """)
    List<DailyBalanceEntity> balances(@Param("periodInitial") LocalDate periodInitial,
                                      @Param("periodFinal") LocalDate periodFinal,
                                      @Param("accounts") List<AccountEntity> accounts);
}
