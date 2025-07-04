package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.AccountBalancePhotoEntity;
import com.ctsousa.mover.core.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccountBalancePhotoRepository extends JpaRepository<AccountBalancePhotoEntity, Long> {

    @Query("""
            SELECT d
            FROM AccountBalancePhotoEntity d
            WHERE d.period BETWEEN :periodInitial AND :periodFinal
              AND d.account IN :accounts
            """)
    List<AccountBalancePhotoEntity> snapshot(@Param("periodInitial") LocalDate periodInitial,
                                                 @Param("periodFinal") LocalDate periodFinal,
                                                 @Param("accounts") List<AccountEntity> accounts);
}
