package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.ClientResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ClientApi {

    @GetMapping("/existing-cpf/{cpf}")
    ResponseEntity<ClientResponse> existingCpfRegister(@PathVariable String cpf);

}
