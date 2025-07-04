package com.ctsousa.mover.integration.corpvs.service.impl;

import com.ctsousa.mover.integration.corpvs.domain.Token;
import com.ctsousa.mover.integration.corpvs.service.CorpvsTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class CorpvsTokenServiceImpl implements CorpvsTokenService {

    @Value("${mover.integration.corpvs.base-url}")
    private String baseUrl;

    @Value("${mover.integration.corpvs.auth-path}")
    private String authPath;

    @Value("${mover.integration.corpvs.email}")
    private String email;

    @Value("${mover.integration.corpvs.password}")
    private String password;

    private Token token;
    private Instant expiration;

    @Override
    public synchronized Token create() {
        if (token == null || Instant.now().isAfter(expiration)) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<?> request = new HttpEntity<>(headers);

            String url = String.format("%s%s?email=%s&password=%s",
                    baseUrl, authPath, email, password);

            RestTemplate template = new RestTemplate();
            ResponseEntity<Token> response = template.postForEntity(url, request, Token.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                this.token = response.getBody();
                this.expiration = Instant.now().plus(55, ChronoUnit.MINUTES);
            } else {
                throw new RuntimeException("Erro ao obter token da Corpvs");
            }
        }
        return token;
    }
}
