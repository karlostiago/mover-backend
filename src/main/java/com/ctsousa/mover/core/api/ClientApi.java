package com.ctsousa.mover.core.api;

import com.ctsousa.mover.request.ClientRequest;
import com.ctsousa.mover.response.ClientResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface ClientApi {

    @GetMapping("/existing-cpf/{cpf}")
    ResponseEntity<ClientResponse> existingCpfRegister(@PathVariable String cpf);

    @PostMapping("/register/client-and-user")
    ResponseEntity<ClientResponse> registerClientAndUser(@RequestBody ClientRequest clientRequest);

    @PutMapping("/{id}")
    ResponseEntity<ClientResponse> updateClient(@PathVariable Long id, @RequestBody ClientRequest clientRequest);
}
