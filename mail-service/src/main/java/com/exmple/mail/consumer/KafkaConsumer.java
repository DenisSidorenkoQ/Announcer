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
public class KafkaConsumer implements Consumer {
    private static final String taskTopic = "${spring.kafka.topic.task.name}";

    private final ObjectMapper objectMapper;
    private final MailService mailService;

    @KafkaListener(topics = taskTopic)
    @Override
    public void consumeMessage(String message) {
        try {
            SendMessageByTimeRequest sendMessageByTimeRequest =
                    objectMapper.readValue(message, SendMessageByTimeRequest.class);
            mailService.sendMessage(sendMessageByTimeRequest.getRecipient(),
                    sendMessageByTimeRequest.getText(),
                    sendMessageByTimeRequest.getTitle()
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
