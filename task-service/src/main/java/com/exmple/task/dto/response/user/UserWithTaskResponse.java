package com.exmple.task.dto.response.user;

import com.exmple.task.entity.Task;
import java.util.List;
import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserWithTaskResponse {
    private String name;
    private String mail;
    private List<Task> tasks;
}
