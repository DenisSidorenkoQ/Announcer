package com.exmple.mail.consumer;

import com.exmple.mail.dto.request.SendMessageByTimeRequest;
import com.exmple.mail.service.MailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailConsumer {
    private static final String taskTopic = "${spring.kafka.topic.task.name}";

    private final ObjectMapper objectMapper;
    private final MailService mailService;

    @KafkaListener(topics = taskTopic)
    public void consumeMessage(String message) throws JsonProcessingException {
        SendMessageByTimeRequest sendMessageByTimeRequest = objectMapper.readValue(message, SendMessageByTimeRequest.class);
        mailService.sendMessage(sendMessageByTimeRequest.getRecipient(),
                sendMessageByTimeRequest.getText(),
                sendMessageByTimeRequest.getTitle()
        );
    }
}
