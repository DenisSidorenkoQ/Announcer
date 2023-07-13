package com.exmple.task.producer;

import com.exmple.task.dto.request.SendMessageByTime;
import com.exmple.task.dto.response.TaskResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Producer {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String taskTopic, SendMessageByTime sendMessageByTime) throws JsonProcessingException {
        String orderAsMessage = objectMapper.writeValueAsString(sendMessageByTime);
        kafkaTemplate.send(taskTopic, orderAsMessage);
    }
}
