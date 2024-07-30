package com.ctsousa.mover.mapper;

import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.mapper.MapperToDomain;
import com.ctsousa.mover.core.mapper.MapperToResponse;
import com.ctsousa.mover.domain.User;
import com.ctsousa.mover.request.UserRequest;
import com.ctsousa.mover.response.UserResponse;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class UserMapper implements MapperToDomain<User, UserRequest>, MapperToResponse<UserResponse, UserEntity> {

    @Override
    public UserResponse toResponse(UserEntity entity) {
        UserResponse response = new UserResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setEmail(entity.getEmail());
        response.setLogin(entity.getLogin());
        response.setClientId(entity.getClientId());
        return response;
    }

    @Override
    public List<UserResponse> toCollections(List<UserEntity> list) {
        return list.stream().map(this::toResponse).toList();
    }

    @Override
    public User toDomain(UserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setPassword(request.getPassword());
        user.setClientId(request.getClientId());
        return user;
    }
}
