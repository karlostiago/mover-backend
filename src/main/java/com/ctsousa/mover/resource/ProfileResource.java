package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.ProfileApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.ProfileEntity;
import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.security.Security;
import com.ctsousa.mover.domain.Profile;
import com.ctsousa.mover.enumeration.Functionality;
import com.ctsousa.mover.request.ProfileRequest;
import com.ctsousa.mover.response.FuncionalityResponse;
import com.ctsousa.mover.response.ProfileResponse;
import com.ctsousa.mover.response.UserResponse;
import com.ctsousa.mover.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;
import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@RestController
@RequestMapping("/profiles")
public class ProfileResource extends BaseResource<ProfileResponse, ProfileRequest, ProfileEntity> implements ProfileApi {

    @Autowired
    private ProfileService profileService;

    public ProfileResource(ProfileService profileService) {
        super(profileService);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Profile.REGISTER_PROFILES)
    public ResponseEntity<ProfileResponse> add(ProfileRequest request) {
        Profile profile = toMapper(request, Profile.class);
        ProfileEntity entity = profileService.save(profile.toEntity());
        return ResponseEntity.ok(toMapper(entity, ProfileResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Profile.UPDATE_PROFILES)
    public ResponseEntity<ProfileResponse> update(Long id, ProfileRequest request) {
        profileService.existsById(id);
        Profile profile = toMapper(request, Profile.class);
        ProfileEntity entity = profile.toEntity();
        profileService.save(entity);
        return ResponseEntity.ok(toMapper(entity, ProfileResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Profile.DELETE_PROFILES)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Profile.FILTER_PROFILES)
    public ResponseEntity<List<ProfileResponse>> filterBy(String search) {
        List<ProfileEntity> entities = profileService.filterBy(search);
        return ResponseEntity.ok(toCollection(entities, ProfileResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Profile.FILTER_PROFILES)
    public ResponseEntity<List<ProfileResponse>> findAll() {
        return super.findAll();
    }

    @Override
    public void updateResponse(List<ProfileResponse> response, List<ProfileEntity> entities) {
        Map<Long, ProfileResponse> responseMap = response.stream()
                .collect(Collectors.toMap(ProfileResponse::getId, r -> r));

        for (ProfileEntity entity : entities) {
            ProfileResponse profileResponse = responseMap.get(entity.getId());
            for (FuncionalityResponse permission : profileResponse.getPermissions()) {
                Functionality functionality = Functionality.find(permission.getName());
                permission.setId(functionality.getCode());
                permission.setName(toUppercase(functionality.getDescription()));
                permission.setCodeMenu(functionality.getMenu().getCode());
                permission.setMenuName(functionality.getMenu().getDescription());
            }
        }
    }

    @Override
    public Class<?> responseClass() {
        return ProfileResponse.class;
    }
}
