package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

//    @Query(value = "SELECT c FROM ClientEntity c WHERE REPLACE(REPLACE(REPLACE(c.cpf, '.', ''), '-', ''), ' ', '') = REPLACE(REPLACE(REPLACE(?1, '.', ''), '-', ''), ' ', '')")
//    ClientEntity existsCpfCnpjRegisteredInApplication(String cpfCnpj);
//
//    boolean existsClientEntityByEmail(String email);

    boolean existsByCpfCnpj(String cpfCnpj);

    boolean existsByEmail(String email);

    @Query("SELECT c FROM ClientEntity c left join fetch c.user where c.cpfCnpj = :cpfCnpj")
    ClientEntity findByCpfCnpj(String cpfCnpj);

    @Query("SELECT CASE WHEN COUNT(c.id) > 0 THEN TRUE ELSE FALSE END FROM ClientEntity c WHERE (c.email = :email OR c.cpfCnpj = :cpfCnpj) AND c.id NOT IN (:id)")
    boolean existsByEmailAndCpfCnpjNotId(@Param("email") String email, @Param("cpfCnpj") String cpfCnpj, @Param("id") Long id);

    @Query("SELECT c FROM ClientEntity c WHERE c.name LIKE %:query% OR c.rg LIKE %:query% OR c.cpfCnpj LIKE %:query% OR c.cellPhone LIKE %:query%")
    List<ClientEntity> findBy(@Param("query") String query);
}
