package com.exmple.task.service;

import com.exmple.task.entity.User;
import com.exmple.task.repository.UserRepository;
import java.util.Optional;
import javax.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> getUserByMail(final String mail) {
        return userRepository.findUserByMail(mail);
    }

    public long addUser(final User userForSave) {
        Optional<User> foundUser = userRepository.findUserByMail(userForSave.getMail());
        if(foundUser.isEmpty()) {
            return userRepository.save(userForSave).getId();
        } else {
            throw new EntityExistsException();
        }

    }
}
