package com.ctsousa.mover.repository;

import com.ctsousa.mover.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Long, ClientEntity> {

    @Query(value = "SELECT * FROM tb_client WHERE cpf = :cpf", nativeQuery = true)
    Optional<ClientEntity> findByCpf(@Param("cpf") String cpf);

}
