package com.exmple.task.converter;

import com.exmple.task.converter.mapper.DateMapper;
import com.exmple.task.dto.request.UpsertTaskRequest;
import com.exmple.task.dto.request.SendMessageRequest;
import com.exmple.task.dto.response.TaskResponse;
import com.exmple.task.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(uses = DateMapper.class)
public interface TaskConverter {
    @Mapping(source = "request.timestamp", target = "time")
    Task fromDto(final UpsertTaskRequest request);

    @Mappings({
            @Mapping(source = "request.timestamp", target = "time")
    })
    Task fromDto(final UpsertTaskRequest request, final int id);

    TaskResponse toTaskResponse(final Task task);

    @Mappings({
            @Mapping(source = "task.mail", target = "recipient")
    })
    SendMessageRequest toSendMessageRequest(final Task task);
}
