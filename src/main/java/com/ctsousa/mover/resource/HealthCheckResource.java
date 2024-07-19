package com.ctsousa.mover.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckResource {

    @GetMapping("/health")
    public String healthCheck() {
        return "API está rodando!";
    }
}
