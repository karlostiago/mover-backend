package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    @Query(value = "SELECT c FROM ClientEntity c WHERE translate(c.cpf, '.-', '') = translate(?1, '.-', '')")
    ClientEntity existsCpfRegisteredInApplication(String cpf);
}
