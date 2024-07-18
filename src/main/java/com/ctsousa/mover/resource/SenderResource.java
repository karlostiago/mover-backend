package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.SenderApi;
import com.ctsousa.mover.core.entity.SenderEntity;
import com.ctsousa.mover.mapper.SenderMapper;
import com.ctsousa.mover.response.SenderResponse;
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
    public ResponseEntity<SenderResponse> sendSecurityCode(Long clientId, String email) {
        SenderEntity entity = service.sendSecurityCode(clientId, email);
        SenderResponse response = mapper.toResponse(entity);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SenderResponse> validateSecurityCode(Long clientId, String email, String code) {
        SenderEntity entity = service.validateSecurityCode(clientId,email, code);
        SenderResponse response = mapper.toResponse(entity);
        return ResponseEntity.ok(response);
    }
}
