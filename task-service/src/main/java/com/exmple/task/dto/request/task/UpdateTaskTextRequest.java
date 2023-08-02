package com.exmple.task.dto.request.task;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateTaskTextRequest {
    private String text;
}
