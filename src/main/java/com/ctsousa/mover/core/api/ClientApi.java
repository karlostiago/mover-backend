package com.ctsousa.mover.core.api;

import com.ctsousa.mover.request.ClientRequest;
import com.ctsousa.mover.response.BrazilianStatesResponse;
import com.ctsousa.mover.response.ClientResponse;
import com.ctsousa.mover.response.TypePersonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ClientApi {

    @GetMapping("/brazilian-states")
    ResponseEntity<List<BrazilianStatesResponse>> findAllBrazilianStates();

    @GetMapping("/types-person")
    ResponseEntity<List<TypePersonResponse>> findAllTypePerson();

    @GetMapping("/addrees/{postalCode}")
    ResponseEntity<ClientResponse> findAddress(@PathVariable("postalCode") Integer postalCode);

    @GetMapping("/filterBy")
    ResponseEntity<List<ClientResponse>> filterBy(@RequestParam("search") String search);

    @GetMapping("/existing-cpf/{cpf}")
    ResponseEntity<ClientResponse> existingCpfRegister(@PathVariable String cpf);

    @PostMapping("/register/client-and-user")
    ResponseEntity<ClientResponse> registerClientAndUser(@RequestBody ClientRequest request);
}
