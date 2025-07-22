package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.FineApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.FineEntity;
import com.ctsousa.mover.domain.Fine;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.request.FineRequest;
import com.ctsousa.mover.response.FineResponse;
import com.ctsousa.mover.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/fines")
public class FineResource extends BaseResource<FineResponse, FineRequest, FineEntity> implements FineApi {

    @Autowired
    private FineService fineService;

    public FineResource(FineService fineService) {
        super(fineService);
    }

    @Override
    public ResponseEntity<FineResponse> add(FineRequest request) {
        Fine fine = toMapper(request, Fine.class);
        Transaction transaction = toMapper(new Transaction().from(fine), Transaction.class);
        FineEntity fineEntity = fineService.save(fine.toEntity(), transaction);
        return ResponseEntity.ok(toMapper(fineEntity, FineResponse.class));
    }

    @Override
    public ResponseEntity<FineResponse> update(Long id, FineRequest request) {
        return null;
    }

    @Override
    public Class<?> responseClass() {
        return FineResponse.class;
    }

    @Override
    public void updateResponse(List<FineResponse> response, List<FineEntity> entities) {
        super.updateResponse(response, entities);
    }
}
