package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    PermissionEntity findByName(String name);

    @Query(value = """
            SELECT tp.name FROM tb_profile p
            INNER JOIN tb_user_profile up ON up.profile_id = p.id
            INNER JOIN tb_profile_permission pf ON pf.profile_id = p.id
            INNER JOIN tb_permission tp ON tp.id = pf.permission_id
            WHERE up.user_id = :userId
            """, nativeQuery = true)
    List<String> findByUser(@Param("userId") Long userId);
}
