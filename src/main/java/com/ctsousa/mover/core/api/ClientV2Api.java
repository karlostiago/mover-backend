package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.BrazilianStatesResponse;
import com.ctsousa.mover.response.ClientV2Response;
import com.ctsousa.mover.response.TypePersonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ClientV2Api {

    @GetMapping("/brazilian-states")
    ResponseEntity<List<BrazilianStatesResponse>> findAllBrazilianStates();

    @GetMapping("/types-person")
    ResponseEntity<List<TypePersonResponse>> findAllTypePerson();

    @GetMapping("/addrees/{postalCode}")
    ResponseEntity<ClientV2Response> findAddress(@PathVariable("postalCode") Integer postalCode);
}
