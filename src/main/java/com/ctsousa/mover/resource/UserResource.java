package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.UserApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.validation.CpfValidator;
import com.ctsousa.mover.request.UserRequest;
import com.ctsousa.mover.response.PermissionResponse;
import com.ctsousa.mover.response.UserResponse;
import com.ctsousa.mover.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<UserResponse> add(UserRequest requestBody) {
        return null;
    }

    @Override
    public ResponseEntity<UserResponse> update(Long id, UserRequest requestBody) {
        return null;
    }

    @Override
    public ResponseEntity<UserResponse> login(String cpf, String password) {
        String formattedCpf = CpfValidator.validateAndFormatCpf(cpf);
        UserEntity entity = userService.login(formattedCpf, password);
        return ResponseEntity.ok(toMapper(entity, UserResponse.class));
    }

    @Override
    public Class<?> responseClass() {
        return UserResponse.class;
    }
}
