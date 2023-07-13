package com.exmple.task.producer;

import com.exmple.task.dto.request.SendMessageRequest;

public interface Producer {
    void sendMessage(String topic, SendMessageRequest sendMessageRequest);
}
