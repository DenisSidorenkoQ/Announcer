package com.exmple.task.dto.request.user;

import javax.validation.constraints.Pattern;
import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InsertUserRequest {
    private String name;
    @Pattern(message = "Email is not valid", regexp =
            "^\\S+@\\S+\\.\\S+$")
    private String mail;
}
