package com.ctsousa.mover.core.api;

import com.ctsousa.mover.request.ClientRequest;
import com.ctsousa.mover.response.ClientResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ClientApi {

    @GetMapping("/existing-cpf/{cpf}")
    ResponseEntity<ClientResponse> existingCpfRegister(@PathVariable String cpf);

    @PostMapping("/register/client-and-user")
    ResponseEntity<ClientResponse> registerClientAndUser(@RequestBody ClientRequest clientRequest);

}
