package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

//    @Query(value = "SELECT c FROM ClientEntity c WHERE REPLACE(REPLACE(REPLACE(c.cpf, '.', ''), '-', ''), ' ', '') = REPLACE(REPLACE(REPLACE(?1, '.', ''), '-', ''), ' ', '')")
//    ClientEntity existsCpfCnpjRegisteredInApplication(String cpfCnpj);
//
//    boolean existsClientEntityByEmail(String email);

}
