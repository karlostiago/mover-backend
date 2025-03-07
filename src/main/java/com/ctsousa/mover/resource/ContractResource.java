package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.ContractApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.ContractEntity;
import com.ctsousa.mover.core.mapper.Transform;
import com.ctsousa.mover.core.security.Security;
import com.ctsousa.mover.domain.Contract;
import com.ctsousa.mover.enumeration.DayOfWeek;
import com.ctsousa.mover.enumeration.PaymentFrequency;
import com.ctsousa.mover.enumeration.Situation;
import com.ctsousa.mover.request.ContractRequest;
import com.ctsousa.mover.response.ContractResponse;
import com.ctsousa.mover.response.DayOfWeekResponse;
import com.ctsousa.mover.response.PaymentFrequencyResponse;
import com.ctsousa.mover.response.SituationResponse;
import com.ctsousa.mover.service.ContractGeneratedSequenceService;
import com.ctsousa.mover.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/contracts")
public class ContractResource extends BaseResource<ContractResponse, ContractRequest, ContractEntity> implements ContractApi {

    @Autowired
    private ContractService contractService;

    private final ContractGeneratedSequenceService generatedSequenceService;

    public ContractResource(ContractService contractService, ContractGeneratedSequenceService generatedSequenceService) {
        super(contractService);
        this.generatedSequenceService = generatedSequenceService;
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Contract.REGISTER_CONTRACTS)
    public ResponseEntity<ContractResponse> add(ContractRequest request) {
        Contract contract = toMapper(request, Contract.class);
        ContractEntity entity = contractService.save(contract.toEntity());
        return ResponseEntity.ok(toMapper(entity, ContractResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Contract.UPDATE_CONTRACTS)
    public ResponseEntity<ContractResponse> update(Long id, ContractRequest request) {
        contractService.existsById(id);
        Contract contract = toMapper(request, Contract.class);
        ContractEntity entity = contract.toEntity();
        contractService.save(entity);
        return ResponseEntity.ok(toMapper(entity, ContractResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Contract.FILTER_CONTRACTS)
    public ResponseEntity<List<ContractResponse>> filterBy(String search) {
        List<ContractEntity> entities = contractService.filterBy(search);
        List<ContractResponse> response = toCollection(entities, ContractResponse.class);
        updateResponse(response, entities);
        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Contract.FILTER_CONTRACTS)
    public ResponseEntity<Optional<ContractResponse>> contractBy(Long clientId) {
        Optional<ContractEntity> contractEntity = contractService.findContratoByClientId(clientId);

        return contractEntity
                .map(entity -> {
                    ContractResponse contractResponse = Transform.toMapper(entity, ContractResponse.class);

                    String fullNameVehicle =
                            entity.getVehicle().getBrand().getName() + " - " +
                            entity.getVehicle().getModel().getName() + " - " +
                            entity.getVehicle().getLicensePlate();

                    contractResponse.setVehicleName(fullNameVehicle);
                    return ResponseEntity.ok(Optional.of(contractResponse));

                }).orElseGet(() -> ResponseEntity.ok(Optional.empty()));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Contract.FILTER_CONTRACTS)
    public ResponseEntity<List<PaymentFrequencyResponse>> paymentFrequencies() {
        List<PaymentFrequency> paymentFrequencies = List.of(PaymentFrequency.values());
        return ResponseEntity.ok(toCollection(paymentFrequencies, PaymentFrequencyResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Contract.FILTER_CONTRACTS)
    public ResponseEntity<List<SituationResponse>> situations() {
        List<Situation> situations = List.of(Situation.CLOSED, Situation.ONGOING);
        return ResponseEntity.ok(toCollection(situations, SituationResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Contract.FILTER_CONTRACTS)
    public ResponseEntity<List<DayOfWeekResponse>> daysOfWeek() {
        List<DayOfWeek> daysOfWeek = List.of(DayOfWeek.values());
        return ResponseEntity.ok(toCollection(daysOfWeek, DayOfWeekResponse.class));
    }

    @Override
    public ResponseEntity<ContractResponse> generatedSequence() {
        return ResponseEntity.ok(generatedSequenceService.generatedNewContractWithSequence());
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Contract.TERMINATION_CONTRACTS)
    public ResponseEntity<ContractResponse> close(Long id, ContractRequest request) {
        contractService.existsById(id);
        Contract contract = toMapper(request, Contract.class);
        ContractEntity entity = contract.toEntity();
        return ResponseEntity.ok(toMapper(contractService.close(entity), ContractResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Contract.DELETE_CONTRACTS)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Contract.FILTER_CONTRACTS)
    public ResponseEntity<List<ContractResponse>> findAll() {
        return super.findAll();
    }

    @Override
    public void updateResponse(List<ContractResponse> response, List<ContractEntity> entities) {
        Map<Long, ContractResponse> responseMap = response.stream()
                .collect(Collectors.toMap(ContractResponse::getId, r -> r));

        for (ContractEntity entity : entities) {
            String fullNameVehicle = entity.getVehicle().getBrand().getName() + " - " +
                    entity.getVehicle().getModel().getName() + " - " +
                    entity.getVehicle().getLicensePlate();
            ContractResponse contractResponse = responseMap.get(entity.getId());
            contractResponse.setVehicleName(fullNameVehicle);
            contractResponse.setPaymentFrequency(entity.getPaymentFrequency());
        }
    }

    @Override
    public Class<?> responseClass() {
        return ContractResponse.class;
    }
}
