package com.exmple.task.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SendTaskMessageResponse {
    private String recipient;
    private String title;
    private String text;
}
