package com.exmple.task.dto.request.task;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateTaskRequest {
    private String title;
    private String text;
    private long timestamp;
}
