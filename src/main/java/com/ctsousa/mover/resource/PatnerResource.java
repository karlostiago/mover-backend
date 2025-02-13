package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.PartnerApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.PartnerEntity;
import com.ctsousa.mover.core.security.Security;
import com.ctsousa.mover.domain.Partner;
import com.ctsousa.mover.request.PartnerRequest;
import com.ctsousa.mover.response.PartnerResponse;
import com.ctsousa.mover.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/partners")
public class PatnerResource extends BaseResource<PartnerResponse, PartnerRequest, PartnerEntity> implements PartnerApi {

    @Autowired
    private PartnerService partnerService;

    public PatnerResource(PartnerService partnerService) {
        super(partnerService);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Partner.REGISTER_PARTNERS)
    public ResponseEntity<PartnerResponse> add(PartnerRequest request) {
        Partner partner = toMapper(request, Partner.class);
        PartnerEntity entity = partnerService.save(partner.toEntity());
        return ResponseEntity.ok(toMapper(entity, PartnerResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Partner.UPDATE_PARTNERS)
    public ResponseEntity<PartnerResponse> update(Long id, PartnerRequest request) {
        partnerService.existsById(id);
        Partner partner = toMapper(request, Partner.class);
        PartnerEntity entity = partner.toEntity();
        partnerService.save(entity);
        return ResponseEntity.ok(toMapper(entity, PartnerResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Partner.FILTER_PARTNERS)
    public ResponseEntity<List<PartnerResponse>> filterBy(String search) {
        List<PartnerEntity> entities = partnerService.filterBy(search);
        return ResponseEntity.ok(toCollection(entities, PartnerResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Partner.DELETE_PARTNERS)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Partner.FILTER_PARTNERS)
    public ResponseEntity<List<PartnerResponse>> findAll() {
        return super.findAll();
    }

    @Override
    public Class<?> responseClass() {
        return PartnerResponse.class;
    }
}
