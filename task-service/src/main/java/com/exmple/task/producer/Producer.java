package com.exmple.task.producer;

import com.exmple.task.dto.response.SendTaskMessageResponse;

public interface Producer {
    void sendTaskMessage(String taskTopic, SendTaskMessageResponse sendTaskMessageResponse);
}
