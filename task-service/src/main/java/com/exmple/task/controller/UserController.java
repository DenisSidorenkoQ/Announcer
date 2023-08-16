package com.exmple.task.controller;

import com.exmple.task.converter.UserConverter;
import com.exmple.task.dto.request.user.InsertUserRequest;
import com.exmple.task.dto.response.user.UserResponse;
import com.exmple.task.entity.User;
import com.exmple.task.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserConverter converter;

    @GetMapping("/{userMail}")
    public UserResponse getUserByMail(@PathVariable final String userMail) {
        User foundUser = userService.getUserByMail(userMail);
        return converter.toUserResponse(foundUser);
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody InsertUserRequest request) {
        User userForSave = converter.fromInsertUserRequestDto(request);

        String savedUserMail = userService.createUser(userForSave);
        return new ResponseEntity(savedUserMail, HttpStatus.CREATED);
    }
}
