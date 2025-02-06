package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.ProfileApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.ProfileEntity;
import com.ctsousa.mover.domain.Profile;
import com.ctsousa.mover.request.ProfileRequest;
import com.ctsousa.mover.response.ProfileResponse;
import com.ctsousa.mover.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/profiles")
public class ProfileResource extends BaseResource<ProfileResponse, ProfileRequest, ProfileEntity> implements ProfileApi {

    @Autowired
    private ProfileService profileService;

    public ProfileResource(ProfileService profileService) {
        super(profileService);
    }

    @Override
    public ResponseEntity<ProfileResponse> add(ProfileRequest request) {
        Profile profile = toMapper(request, Profile.class);
        ProfileEntity entity = profileService.save(profile.toEntity());
        return ResponseEntity.ok(toMapper(entity, ProfileResponse.class));
    }

    @Override
    public ResponseEntity<ProfileResponse> update(Long id, ProfileRequest request) {
        profileService.existsById(id);
        Profile profile = toMapper(request, Profile.class);
        ProfileEntity entity = profile.toEntity();
        profileService.save(entity);
        return ResponseEntity.ok(toMapper(entity, ProfileResponse.class));
    }

    @Override
    public Class<?> responseClass() {
        return ProfileResponse.class;
    }
}
