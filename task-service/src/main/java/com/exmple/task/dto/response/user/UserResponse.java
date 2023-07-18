package com.exmple.task.dto.response.user;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    private String name;
    private String mail;
}
