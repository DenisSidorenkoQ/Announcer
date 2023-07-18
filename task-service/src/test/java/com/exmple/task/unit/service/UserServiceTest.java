package com.exmple.task.unit.service;

import com.exmple.task.entity.User;
import com.exmple.task.repository.UserRepository;
import com.exmple.task.service.UserService;
import java.util.Optional;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Test;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    public void testGetUserByMailWhenExist() {
        String mail = "test@example.com";
        User user = new User();
        user.setMail(mail);
        when(userRepository.findUserByMail(mail)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByMail(mail);

        assertTrue(result.isPresent());
        assertEquals(mail, result.get().getMail());
    }

    @Test
    public void testGetUserByMailWhenNotExist() {
        String mail = "test@example.com";
        when(userRepository.findUserByMail(mail)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByMail(mail);

        assertFalse(result.isPresent());
    }

    @Test
    public void testAddUser() {
        User user = new User();
        user.setMail("test@example.com");
        when(userRepository.save(user)).thenReturn(user);

        long userId = userService.addUser(user);

        assertEquals(user.getId(), userId);
    }
}
