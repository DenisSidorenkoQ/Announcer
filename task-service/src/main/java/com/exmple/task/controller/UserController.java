package com.exmple.task.controller;

import com.exmple.task.converter.UserConverter;
import com.exmple.task.dto.request.user.InsertUserRequest;
import com.exmple.task.entity.User;
import com.exmple.task.service.UserService;
import java.util.Optional;
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

    @GetMapping
    public ResponseEntity getUserByMail(@RequestParam("mail") final String mail) {
        User foundUser = userService.getUserByMail(mail);
        return new ResponseEntity(converter.toUserResponse(foundUser), HttpStatus.OK);
    }

    @GetMapping("/tasks")
    public ResponseEntity getUserWithTasksByMail(@RequestParam("mail") final String mail) {
        User foundUser = userService.getUserByMail(mail);
        return new ResponseEntity(converter.toUserWithTaskResponseDto(foundUser), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody InsertUserRequest request) {
        User userForSave = converter.fromInsertUserRequestDto(request);

        String savedUserMail = userService.createUser(userForSave);
        return new ResponseEntity(savedUserMail, HttpStatus.CREATED);
    }
}
