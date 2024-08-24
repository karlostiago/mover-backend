package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.FipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface FipeRepository extends JpaRepository<FipeEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(f.id) > 0 THEN TRUE ELSE FALSE END FROM FipeEntity f WHERE f.code = :code")
    boolean existsByCode(@Param("code") String code);

    @Query("SELECT CASE WHEN COUNT(f.id) > 0 THEN TRUE ELSE FALSE END FROM FipeEntity f WHERE f.hash = :hash")
    boolean existsByHash(@Param("hash") String hash);

    FipeEntity findByHash(String hash);

    FipeEntity findByCode(String code);

    @Query(value = """
                SELECT
                	f.*
                FROM tb_fipe f
                JOIN tb_vehicle v ON v.model_year = f.model_year
                JOIN tb_brand b ON b.id = v.brand_id
                JOIN tb_model m ON m.id = v.model_id
                WHERE v.id = :vehicleId
                  AND f.brand = b.name
                  AND f.model = m.name
                  AND f.reference_year = YEAR(:reference)
                  AND f.reference_month = CASE
                        WHEN MONTH(:reference) = 1 THEN 'JANEIRO'
                        WHEN MONTH(:reference) = 2 THEN 'FEVEREIRO'
                        WHEN MONTH(:reference) = 3 THEN 'MARÇO'
                        WHEN MONTH(:reference) = 4 THEN 'ABRIL'
                        WHEN MONTH(:reference) = 5 THEN 'MAIO'
                        WHEN MONTH(:reference) = 6 THEN 'JUNHO'
                        WHEN MONTH(:reference) = 7 THEN 'JULHO'
                        WHEN MONTH(:reference) = 8 THEN 'AGOSTO'
                        WHEN MONTH(:reference) = 9 THEN 'SETEMBRO'
                        WHEN MONTH(:reference) = 10 THEN 'OUTUBRO'
                        WHEN MONTH(:reference) = 11 THEN 'NOVEMBRO'
                        WHEN MONTH(:reference) = 12 THEN 'DEZEMBRO'
                        ELSE NULL
                    END;
            """,
            nativeQuery = true)
    FipeEntity findByVehicleAndReference(@Param("vehicleId") Long vehicleId, @Param("reference")LocalDate reference);
}
