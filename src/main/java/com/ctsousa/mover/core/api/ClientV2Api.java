package com.ctsousa.mover.core.api;

import com.ctsousa.mover.request.ClientV2Request;
import com.ctsousa.mover.response.BrazilianStatesResponse;
import com.ctsousa.mover.response.ClientV2Response;
import com.ctsousa.mover.response.TypePersonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ClientV2Api {

    @GetMapping("/brazilian-states")
    ResponseEntity<List<BrazilianStatesResponse>> findAllBrazilianStates();

    @GetMapping("/types-person")
    ResponseEntity<List<TypePersonResponse>> findAllTypePerson();

    @GetMapping("/addrees/{postalCode}")
    ResponseEntity<ClientV2Response> findAddress(@PathVariable("postalCode") Integer postalCode);

    @GetMapping("/filterBy")
    ResponseEntity<List<ClientV2Response>> filterBy(@RequestParam("search") String search);

    @GetMapping("/existing-cpf/{cpf}")
    ResponseEntity<ClientV2Response> existingCpfRegister(@PathVariable String cpf);

    @PostMapping("/register/client-and-user")
    ResponseEntity<ClientV2Response> registerClientAndUser(@RequestBody ClientV2Request request);
}
