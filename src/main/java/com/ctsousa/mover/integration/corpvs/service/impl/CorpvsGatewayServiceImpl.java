package com.ctsousa.mover.integration.corpvs.service.impl;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.integration.corpvs.CorpvsGateway;
import com.ctsousa.mover.integration.corpvs.domain.Token;
import com.ctsousa.mover.integration.corpvs.domain.Vehicle;
import com.ctsousa.mover.integration.corpvs.response.CorpvsResponse;
import com.ctsousa.mover.integration.corpvs.service.CorpvsTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class CorpvsGatewayServiceImpl implements CorpvsGateway {

    @Value("${mover.integration.corpvs.base-url}")
    private String baseUrl;

    @Value("${mover.integration.corpvs.report-path}")
    private String reportPath;

    @Value("${mover.integration.corpvs.registration-path}")
    private String registrationPath;

    private final CorpvsTokenService tokenService;

    public CorpvsGatewayServiceImpl(CorpvsTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Double totalOdometer(Integer veiId, LocalDate date) {
        try {
            Token token = tokenService.create();

            String start = date.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String end = date.atTime(LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String url = UriComponentsBuilder
                    .fromHttpUrl(baseUrl + reportPath)
                    .queryParam("CLI_ID", token.getData().getCliId())
                    .queryParam("VEI_ID", veiId)
                    .queryParam("start_time", start)
                    .queryParam("end_time", end)
                    .build(false)
                    .toUriString();

            HttpEntity<?> entity = new HttpEntity<>(getHeaders(token.getToken()));

            ResponseEntity<CorpvsResponse> response = restTemplate().exchange(url, HttpMethod.GET, entity, CorpvsResponse.class);
            CorpvsResponse corpvsResponse = response.getBody();

            assert corpvsResponse != null;
            BigDecimal total = BigDecimal.valueOf(corpvsResponse.getData().get(0)
                    .getDistance().doubleValue()).divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP);
            return total.doubleValue();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new NotificationException(ex.getMessage());
        }
    }

    @Override
    public List<Vehicle> allVehicles() {
        try {
            Token token = tokenService.create();

            String url = UriComponentsBuilder
                    .fromHttpUrl(baseUrl + registrationPath)
                    .queryParam("CLI_ID", token.getData().getCliId())
                    .build(false)
                    .toUriString();

            HttpEntity<?> entity = new HttpEntity<>(getHeaders(token.getToken()));

            ResponseEntity<CorpvsResponse> response = restTemplate().exchange(url, HttpMethod.GET, entity, CorpvsResponse.class);
            CorpvsResponse corpvsResponse = response.getBody();

            assert corpvsResponse != null;
            return corpvsResponse.getVehicles();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new NotificationException(ex.getMessage());
        }
    }

    private HttpHeaders getHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.ALL));
        return headers;
    }

    private RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
