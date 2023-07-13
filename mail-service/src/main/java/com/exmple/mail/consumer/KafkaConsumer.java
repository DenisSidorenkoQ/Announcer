package com.exmple.mail.consumer;

import com.exmple.mail.dto.request.SendTaskMessageRequest;
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
            SendTaskMessageRequest sendTaskMessageRequest =
                    objectMapper.readValue(message, SendTaskMessageRequest.class);
            mailService.sendMessage(sendTaskMessageRequest.getRecipient(),
                    sendTaskMessageRequest.getText(),
                    sendTaskMessageRequest.getTitle()
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
