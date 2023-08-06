package com.exmple.task.dto.request.task;

import com.exmple.task.entity.EStatus;
import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateTaskStatusRequest {
    private EStatus status;
}
