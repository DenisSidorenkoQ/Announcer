package com.exmple.mail.consumer;

import com.exmple.mail.dto.request.SendMessageRequest;
import com.exmple.mail.service.MailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer implements Consumer {
    private static final String taskTopic = "${spring.kafka.topic.task.name}";

    private final ObjectMapper objectMapper;
    private final MailService mailService;

    @KafkaListener(topics = taskTopic)
    @Override
    public void consumeTaskMessage(String message) {
        try {
            SendMessageRequest sendMessageRequest =
                    objectMapper.readValue(message, SendMessageRequest.class);
            mailService.sendMessage(sendMessageRequest.getRecipient(),
                    sendMessageRequest.getText(),
                    sendMessageRequest.getTitle()
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
