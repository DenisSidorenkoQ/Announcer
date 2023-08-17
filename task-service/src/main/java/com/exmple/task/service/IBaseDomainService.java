package com.exmple.task.service;

import com.exmple.task.entity.Domain;

public interface IBaseDomainService<T extends Domain> {

    T create(T domain);

}
