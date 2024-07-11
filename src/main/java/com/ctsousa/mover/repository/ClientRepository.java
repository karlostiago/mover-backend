package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.service.querys.ClientQuerys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long>, ClientQuerys {

    @Query(value = EXISTING_CPF_CLIENT_APPLICATION)
    ClientEntity existsCpfRegisteredInApplication(String cpf);
}
