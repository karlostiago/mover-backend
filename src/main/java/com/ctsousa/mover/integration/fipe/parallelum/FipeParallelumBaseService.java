package com.ctsousa.mover.integration.fipe.parallelum;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public abstract class FipeParallelumBaseService {

    protected final RestTemplate restTemplate = new RestTemplate();

    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public abstract String pathBase();

    protected <T> List<T> requestProcess(final String path, ParameterizedTypeReference<List<T>> responseType) {
        var response = restTemplate.exchange(
                path,
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()),
                responseType
        );

        return response.getBody();
    }

    protected <T> T requestProcess(final String path, Class<T> responseType) {
        var response = restTemplate.exchange(
                path,
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()),
                responseType
        );

        return response.getBody();
    }
}
