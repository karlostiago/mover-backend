package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.FineApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.FineEntity;
import com.ctsousa.mover.request.FineRequest;
import com.ctsousa.mover.response.FineResponse;
import com.ctsousa.mover.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fines")
public class FineResource extends BaseResource<FineResponse, FineRequest, FineEntity> implements FineApi {

    @Autowired
    private FineService fineService;

    public FineResource(FineService fineService) {
        super(fineService);
    }

    @Override
    public ResponseEntity<FineResponse> add(FineRequest requestBody) {
        return null;
    }

    @Override
    public ResponseEntity<FineResponse> update(Long id, FineRequest requestBody) {
        return null;
    }

    @Override
    public Class<?> responseClass() {
        return FineResponse.class;
    }
}
