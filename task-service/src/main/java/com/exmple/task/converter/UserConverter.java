package com.exmple.task.converter;

import com.exmple.task.dto.request.user.InsertUserRequest;
import com.exmple.task.dto.response.user.UserResponse;
import com.exmple.task.dto.response.user.UserWithTaskResponse;
import com.exmple.task.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserConverter {
    UserWithTaskResponse toUserWithTaskResponseDto(User user);

    User fromInsertUserRequestDto(InsertUserRequest request);

    UserResponse toUserResponse(User user);
}
