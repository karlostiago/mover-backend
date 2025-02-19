package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.ChangePasswordApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.security.Security;
import com.ctsousa.mover.domain.User;
import com.ctsousa.mover.request.UserRequest;
import com.ctsousa.mover.response.ProfileResponse;
import com.ctsousa.mover.response.UserResponse;
import com.ctsousa.mover.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/changepasswords")
public class ChangePasswordResource extends BaseResource<UserResponse, UserRequest, UserEntity> implements ChangePasswordApi {

    private final UserService userService;

    public ChangePasswordResource(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @Override
    public ResponseEntity<UserResponse> add(UserRequest request) {
        throw new NotificationException("Não suportado.");
    }

    @Override
    public ResponseEntity<UserResponse> update(Long id, UserRequest request) {
        throw new NotificationException("Não suportado.");
    }

    @Override
    @PreAuthorize(Security.PreAutorize.ChangePassword.FILTER_USERS)
    public ResponseEntity<List<UserResponse>> findAll() {
        return super.findAll();
    }

    @Override
    @PreAuthorize(Security.PreAutorize.ChangePassword.UPDATE_CHANGEPASSWORD_USERS)
    public ResponseEntity<Void> changePassword(UserRequest request) {
        User domain = toMapper(request, User.class);
        userService.changePassword(domain.toEntity());
        return ResponseEntity.ok().build();
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
