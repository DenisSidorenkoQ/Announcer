package com.exmple.task.service;

import com.exmple.task.entity.Domain;
import com.exmple.task.exception.ErrorMessages;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

public abstract class BaseDomainService<T extends Domain> implements IBaseDomainService<T> {

    private IBaseDomainRepository baseRepository;

    private T get(long id) {
        Optional<T> foundTask = baseRepository.findById(id);
        return foundTask.orElseThrow(() -> new EntityNotFoundException(ErrorMessages.TASK_NOT_FOUND));
    }

}
