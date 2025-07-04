package com.ctsousa.mover.integration.parallelum.service;

import com.ctsousa.mover.integration.parallelum.util.DateUtil;
import com.ctsousa.mover.integration.parallelum.ParallelumGateway;
import com.ctsousa.mover.integration.parallelum.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.ctsousa.mover.core.util.StringUtil.removeLastPoint;
import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Slf4j
@Service
public class ParallelumService implements ParallelumGateway {

    @Value("${mover.integration.parallelum.base-url}")
    private String baseUrl;

    @Value("${mover.integration.parallelum.car-path}")
    private String carPath;

    @Value("${mover.integration.parallelum.reference-path}")
    private String referencePath;

    private final Object brandLock = new Object();
    private final Object modelLock = new Object();
    private final Object referenceLock = new Object();

    private final Map<String, List<Year>> yearCache = new ConcurrentHashMap<>();

    @Override
    public List<Brand> allBrands() {
        log.info("Carregando todas as marcas...");
        String url = UriComponentsBuilder
                .fromHttpUrl(baseUrl + carPath)
                .build(false)
                .toUriString();

        HttpEntity<?> entity = new HttpEntity<>(getHeaders());
        ParameterizedTypeReference<List<Brand>> typeRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Brand>> response = restTemplate().exchange(url, HttpMethod.GET, entity, typeRef);

        return new ArrayList<>(Objects.requireNonNull(response.getBody()));
    }

    @Override
    public List<Reference> allReference() {
        log.info("Carregando todas as referencias...");

        log.info("Carregando todas as referencias...");
        String url = UriComponentsBuilder
                .fromHttpUrl(baseUrl + referencePath)
                .build(false)
                .toUriString();

        HttpEntity<?> entity = new HttpEntity<>(getHeaders());
        ParameterizedTypeReference<List<Reference>> typeRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Reference>> response = restTemplate().exchange(url, HttpMethod.GET, entity, typeRef);

        return new ArrayList<>(Objects.requireNonNull(response.getBody()));
    }

    @Override
    public List<Model> listModels(String codeBrand) {
        log.info("Carregando os modelos, código marca {} ", codeBrand);

        String url = UriComponentsBuilder
                .fromHttpUrl(baseUrl + carPath)
                .pathSegment(codeBrand, "models")
                .build(false)
                .toUriString();

        HttpEntity<?> entity = new HttpEntity<>(getHeaders());

        ParameterizedTypeReference<List<Model>> parameterizedTypeReference = new ParameterizedTypeReference<>() { };
        ResponseEntity<List<Model>> response = restTemplate().exchange(url, HttpMethod.GET, entity, parameterizedTypeReference);

        return new ArrayList<>(Objects.requireNonNull(response.getBody()));
    }

    @Override
    public List<Year> listYear(String codeBrand, String codeModel) {
        String cacheKey = codeBrand + "-" + codeModel;

        return yearCache.computeIfAbsent(cacheKey, key -> {
            log.info("Carregando os anos, código marca {} e código modelo {} ", codeBrand, codeModel);

            String url = UriComponentsBuilder
                    .fromHttpUrl(baseUrl + carPath)
                    .pathSegment( codeBrand, "models", codeModel, "years")
                    .build(false)
                    .toUriString();

            HttpEntity<?> entity = new HttpEntity<>(getHeaders());
            ParameterizedTypeReference<List<Year>> typeRef = new ParameterizedTypeReference<>() {};
            ResponseEntity<List<Year>> response = restTemplate().exchange(url, HttpMethod.GET, entity, typeRef);

            return new ArrayList<>(Objects.requireNonNull(response.getBody()));
        });
    }

    @Override
    public Brand findBrand(String brandName) {
        return allBrands().stream()
                .filter(b -> b.getName().toUpperCase().contains(brandName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Year findYear(String codeBrand, String codeModel, Integer modelYear, String fuelType) {
        log.info("Buscando referencias anuais...");
        String nameFilter = modelYear.toString().concat(" ").concat(fuelType);
        return listYear(codeBrand, codeModel).stream()
                .filter(y -> toUppercase(y.getName()).equalsIgnoreCase(nameFilter))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Model findModel(String codeBrand, String modelName) {
        log.info("Buscando modelos...");
        return listModels(codeBrand).stream()
                .filter(m -> removeLastPoint(m.getName()).equalsIgnoreCase(modelName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Reference findReference(LocalDate reference) {
        log.info("Buscando referencias...");
        String monthAndYearOfReference = getMonthYearOfReference(reference);
        return allReference().stream()
                .filter(r -> r.getMonth().equalsIgnoreCase(monthAndYearOfReference))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Fipe findFipe(String codeBrand, String codeModel, String codeYear, String codeReference) {
        log.info("Carregando fipe, código marca {}, código modelo {} e código ano {} da api fipe parallelum ", codeBrand, codeModel, codeYear);

        String url = UriComponentsBuilder
                .fromHttpUrl(baseUrl + carPath)
                .pathSegment( codeBrand, "models", codeModel, "years", codeYear)
                .queryParam("reference", codeReference)
                .build(false)
                .toUriString();

        HttpEntity<?> entity = new HttpEntity<>(getHeaders());

        ResponseEntity<Fipe> response = restTemplate().exchange(url, HttpMethod.GET, entity, Fipe.class);
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

    private String getMonthYearOfReference(LocalDate reference) {
        String month = DateUtil.toMonthPtBr(reference);
        int year = reference.getYear();
        return month + "/" + year;
    }
}
