package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.SenderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

public interface SenderApi {

    @PostMapping("/send-security-code/{clientId}/{email:.+}")
    ResponseEntity<SenderResponse> sendSecurityCode(@PathVariable Long clientId, @PathVariable String email);

    @PostMapping("/validate-security-code/{clientId}/{email:.+}/{code}")
    ResponseEntity<SenderResponse> validateSecurityCode(@PathVariable Long clientId, @PathVariable String email, @PathVariable String code);

}
