package com.exmple.task.converter;

import com.exmple.task.converter.mapper.DateMapper;
import com.exmple.task.dto.request.task.InsertTaskRequest;
import com.exmple.task.dto.request.SendMessageByTime;
import com.exmple.task.dto.response.task.TaskResponse;
import com.exmple.task.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(uses = DateMapper.class)
public interface TaskConverter {
    @Mappings({
        @Mapping(source = "request.timestamp", target = "time")
    })
    Task fromDto(final InsertTaskRequest request, String userMail);

    @Mappings({
            @Mapping(source = "task.userMail", target = "recipient")
    })
    SendMessageByTime toSendMessageByTimeDto(final Task task);

    TaskResponse toTaskResponseDto(final Task task);
}
