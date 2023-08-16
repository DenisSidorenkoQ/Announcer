package com.exmple.task.service;

import com.exmple.task.dto.request.SendMessageByTime;
import com.exmple.task.entity.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class RestNotificationService implements NotificationService {
    private final RestTemplate restTemplate;

    @Value("${mail-service.send-by-time.url}")
    private String sendMailURI;

    @Override
    public void sendTaskNotification(SendMessageByTime message) throws Exception {
        ResponseEntity<SendMessageByTime> sendMailResponse =
                restTemplate.postForEntity(sendMailURI, message, SendMessageByTime.class);
        if (sendMailResponse.getStatusCode() != HttpStatus.OK) {
            throw new Exception();
        }
    }
}
