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
    public ResponseEntity getUser(@RequestParam("mail") final String mail) {
        Optional<User> foundUser = userService.getUserByMail(mail);

        return foundUser
                .map(user -> new ResponseEntity(converter.toUserResponse(user), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/tasks")
    public ResponseEntity getUserWithTasksByMail(@RequestParam("mail") final String mail) {
        Optional<User> foundUser = userService.getUserByMail(mail);

        return foundUser
                .map(user -> new ResponseEntity(converter.toUserWithTaskResponseDto(user), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity addUser(@RequestBody InsertUserRequest request) {
        User userForSave = converter.fromInsertUserRequestDto(request);

        try {
            long savedUserId = userService.addUser(userForSave);
            return new ResponseEntity(savedUserId, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }
}
