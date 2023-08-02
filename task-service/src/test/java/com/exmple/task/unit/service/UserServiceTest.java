package com.exmple.task.unit.service;

import com.exmple.task.entity.User;
import com.exmple.task.repository.UserRepository;
import com.exmple.task.service.UserService;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
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

        User result = userService.getUserByMail(mail);

        assertEquals(mail, result.getMail());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetUserByMailWhenNotExist() {
        String mail = "test@example.com";
        when(userRepository.findUserByMail(mail)).thenReturn(Optional.empty());

        User result = userService.getUserByMail(mail);
    }

    @Test
    public void testAddUser() {
        User user = new User();
        user.setMail("test@example.com");
        when(userRepository.save(user)).thenReturn(user);

        String userMail = userService.createUser(user);

        assertEquals(user.getMail(), userMail);
    }
}
