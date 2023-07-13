package com.exmple.task.producer;

import com.exmple.task.dto.request.SendMessageByTime;

public interface Producer {
    void sendMessage(String taskTopic, SendMessageByTime sendMessageByTime);
}
