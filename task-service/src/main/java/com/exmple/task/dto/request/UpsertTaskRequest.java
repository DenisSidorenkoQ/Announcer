package com.exmple.task.dto.request;

import javax.validation.constraints.Pattern;
import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpsertTaskRequest {
    @Pattern(message = "Email is not valid", regexp =
            "^\\S+@\\S+\\.\\S+$")
    private String mail;
    private String title;
    private String text;
    private long timestamp;
}
