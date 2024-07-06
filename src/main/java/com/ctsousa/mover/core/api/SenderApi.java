package com.ctsousa.mover.core.api;

import com.ctsousa.mover.domain.Sender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface SenderApi {

    @PostMapping("/send-security-code")
    ResponseEntity<Sender> sendSecurityCode(@RequestParam Long clientId, @RequestParam String email);

    @PostMapping("/validate-security-code")
    ResponseEntity<Sender> validateSecurityCode(@RequestParam Long clientId, @RequestParam String email, @RequestParam String code);

}
