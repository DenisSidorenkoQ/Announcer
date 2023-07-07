package com.exmple.task.dto;

import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpsertTaskRequest {
    @Pattern(message = "Email is not valid", regexp =
            "^\\S+@\\S+\\.\\S+$")
    private String mail;
    private String text;
    private long time;
}
