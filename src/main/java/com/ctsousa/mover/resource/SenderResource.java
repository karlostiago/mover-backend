package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.SenderApi;
import com.ctsousa.mover.domain.Sender;
import com.ctsousa.mover.mapper.SenderMapper;
import com.ctsousa.mover.service.SenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sender")
public class SenderResource implements SenderApi {

    private final SenderMapper mapper;
    private final SenderService service;

    public SenderResource(SenderMapper mapper, SenderService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @Override
    public ResponseEntity<Sender> sendSecurityCode(Long clientId, String email) {
        return ResponseEntity.ok(mapper.toDomain(service.sendSecurityCode(clientId, email)));
    }

    @Override
    public ResponseEntity<Sender> validateSecurityCode(Long clientId, String email, String code) {
        return ResponseEntity.ok(mapper.toDomain(service.validateSecurityCode(clientId,email, code)));
    }
}
