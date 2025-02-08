package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.UserApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.validation.CpfValidator;
import com.ctsousa.mover.domain.User;
import com.ctsousa.mover.request.UserRequest;
import com.ctsousa.mover.response.ProfileResponse;
import com.ctsousa.mover.response.UserResponse;
import com.ctsousa.mover.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/users")
public class UserResource extends BaseResource<UserResponse, UserRequest, UserEntity> implements UserApi {

    private final UserService userService;

    public UserResource(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @Override
    public ResponseEntity<UserResponse> add(UserRequest request) {
        User user = toMapper(request, User.class);
        UserEntity entity = userService.save(user.toEntity());
        return ResponseEntity.ok(toMapper(entity, UserResponse.class));
    }

    @Override
    public ResponseEntity<UserResponse> update(Long id, UserRequest request) {
        userService.existsById(id);
        User domain = toMapper(request, User.class);
        UserEntity entity = domain.toEntity();
        userService.save(entity);
        return ResponseEntity.ok(toMapper(entity, UserResponse.class));
    }

    @Override
    public ResponseEntity<UserResponse> login(String cpf, String password) {
        String formattedCpf = CpfValidator.validateAndFormatCpf(cpf);
        UserEntity entity = userService.login(formattedCpf, password);
        return ResponseEntity.ok(toMapper(entity, UserResponse.class));
    }

    @Override
    public ResponseEntity<List<UserResponse>> filterBy(String search) {
        List<UserEntity> entities = userService.filterBy(search);
        return ResponseEntity.ok(toCollection(entities, UserResponse.class));
    }

    @Override
    public void updateResponse(List<UserResponse> response, List<UserEntity> entities) {
        Map<Long, UserResponse> responseMap = response.stream()
                .collect(Collectors.toMap(UserResponse::getId, r -> r));
        for (UserEntity entity : entities) {
            UserResponse user = responseMap.get(entity.getId());
            List<ProfileResponse> profiles = entity.getProfiles().stream()
                    .map(p -> new ProfileResponse(p.getId(), p.getDescription(), p.getActive())).toList();
            user.setProfiles(profiles);
        }
    }

    @Override
    public Class<?> responseClass() {
        return UserResponse.class;
    }
}
