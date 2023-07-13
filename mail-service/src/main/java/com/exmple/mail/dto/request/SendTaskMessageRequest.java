package com.exmple.mail.dto.request;

import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendTaskMessageRequest {
    @Pattern(message = "Recipient is not valid", regexp =
            "^\\S+@\\S+\\.\\S+$")
    private String recipient;
    private String title;
    private String text;
}
