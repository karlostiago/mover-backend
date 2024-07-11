package com.ctsousa.mover.core.api;

import com.ctsousa.mover.domain.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface ClientApi {

    @GetMapping("/existing-cpf/{cpf}")
    ResponseEntity<Client> existingCpfRegister(@PathVariable String cpf);

}
