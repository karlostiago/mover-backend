package com.ctsousa.mover.integration.viacep.service;

import com.ctsousa.mover.integration.viacep.ViaCepGateway;
import com.ctsousa.mover.integration.viacep.domain.ViaCep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class ViaCepService implements ViaCepGateway {

    @Value("${viacep.base-url}")
    private String baseUrl;

    @Override
    public ViaCep findPostalCode(Integer postalCode) {
        log.info("Buscando cep {} pela integracao viacep ", postalCode);

        String url = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .pathSegment(postalCode.toString(), "json/")
                .build(false)
                .toUriString();

        HttpEntity<?> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<ViaCep> response = restTemplate().exchange(url, HttpMethod.GET, entity, ViaCep.class);

        return response.getBody();

    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
