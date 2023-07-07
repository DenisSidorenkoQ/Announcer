package com.exmple.task.converter;

import com.exmple.task.converter.mapper.DateMapper;
import com.exmple.task.dto.UpsertTaskRequest;
import com.exmple.task.entity.Task;
import org.mapstruct.Mapper;

@Mapper(uses = DateMapper.class)
public interface TaskConverter {
    Task fromDto(final UpsertTaskRequest request);

    Task fromDto(final UpsertTaskRequest request, final int id);
}
