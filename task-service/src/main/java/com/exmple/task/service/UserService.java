package com.exmple.task.service;

import com.exmple.task.exception.ErrorMessages;
import com.exmple.task.entity.User;
import com.exmple.task.repository.UserRepository;
import java.util.Optional;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserByMail(final String mail) {
        Optional<User> user = userRepository.findUserByMail(mail);
        if(user.isPresent()) {
            return user.get();
        } else {
            throw new EntityNotFoundException(ErrorMessages.USER_NOT_FOUND);
        }
    }

    public String createUser(final User userForSave) throws EntityNotFoundException {
        Optional<User> foundUser = userRepository.findUserByMail(userForSave.getMail());
        if(foundUser.isEmpty()) {
            return userRepository.save(userForSave).getMail();
        } else {
            throw new EntityExistsException(ErrorMessages.USER_IS_EXISTS);
        }
    }
}
