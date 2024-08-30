package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.UserApi;
import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.validation.CpfValidator;
import com.ctsousa.mover.mapper.UserMapper;
import com.ctsousa.mover.response.ClientResponse;
import com.ctsousa.mover.response.UserResponse;
import com.ctsousa.mover.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserResource implements UserApi {

    private final UserService userService;
    private final UserMapper userMapper;


    public UserResource(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public ResponseEntity<UserResponse> login(String cpf, String password) {
        String formattedCpf = CpfValidator.validateAndFormatCpf(cpf);

        UserEntity user = userService.login(formattedCpf, password);
        UserResponse response  = userMapper.toResponse(user);

        return ResponseEntity.ok(response);
    }
}
