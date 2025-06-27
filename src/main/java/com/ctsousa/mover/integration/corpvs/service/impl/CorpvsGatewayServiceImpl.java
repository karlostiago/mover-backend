package com.ctsousa.mover.integration.corpvs.service.impl;

import com.ctsousa.mover.integration.corpvs.CorpvsGateway;
import com.ctsousa.mover.integration.corpvs.response.CorpvsResponse;
import com.ctsousa.mover.integration.corpvs.service.CorpvsTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class CorpvsGatewayServiceImpl implements CorpvsGateway {

    @Value("${corpvs.base-url}")
    private String baseUrl;

    @Value("${corpvs.report-path}")
    private String reportPath;

    private final CorpvsTokenService tokenService;

    public CorpvsGatewayServiceImpl(CorpvsTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Double totalOdometer(String cliId, String veiId, LocalDate date) {
        try {
            String start = date.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String end = date.atTime(LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String url = UriComponentsBuilder
                    .fromHttpUrl(baseUrl + reportPath)
                    .queryParam("CLI_ID", cliId)
                    .queryParam("VEI_ID", veiId)
                    .queryParam("start_time", start)
                    .queryParam("end_time", end)
                    .build(false)
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(tokenService.create());
            headers.setAccept(List.of(MediaType.ALL));

            HttpEntity<?> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<CorpvsResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, CorpvsResponse.class);
            CorpvsResponse corpvsResponse = response.getBody();

            System.out.println(corpvsResponse);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.out.println(ex);
        } catch (Exception e) {
            System.out.println(e);
        }

        return 0.0;
    }
}
