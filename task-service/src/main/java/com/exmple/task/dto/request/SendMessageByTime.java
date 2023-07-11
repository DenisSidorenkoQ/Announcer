package com.exmple.task.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SendMessageByTime {
    private String recipient;
    private String title;
    private String text;
}
