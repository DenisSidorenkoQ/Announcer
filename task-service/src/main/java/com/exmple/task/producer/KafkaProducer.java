package com.exmple.task.producer;

import com.exmple.task.dto.response.SendTaskMessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableAsync
public class KafkaProducer implements Producer {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Async
    @Override
    public void sendTaskMessage(String taskTopic, SendTaskMessageResponse sendTaskMessageResponse) {;
        try {
            String message = objectMapper.writeValueAsString(sendTaskMessageResponse);
            kafkaTemplate.send(taskTopic, message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
