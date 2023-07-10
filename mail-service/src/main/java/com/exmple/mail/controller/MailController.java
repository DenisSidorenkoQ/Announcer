package com.exmple.mail.controller;

import com.exmple.mail.dto.request.SendMessageByTimeRequest;
import com.exmple.mail.service.MailService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/mails")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @PostMapping("/by-time/send")
    public void sendMessage(@RequestBody @Valid SendMessageByTimeRequest request) {
        mailService.sendMessage(request.getRecipient(), request.getText(), request.getTitle());
    }
}
