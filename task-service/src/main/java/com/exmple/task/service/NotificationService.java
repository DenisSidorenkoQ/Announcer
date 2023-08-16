package com.exmple.task.service;

import com.exmple.task.dto.request.SendMessageByTime;

public interface NotificationService {
    void sendTaskNotification(SendMessageByTime message) throws Exception;
}
