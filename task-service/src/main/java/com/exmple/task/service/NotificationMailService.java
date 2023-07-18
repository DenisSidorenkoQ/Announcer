package com.exmple.task.service;

import com.exmple.task.converter.TaskConverter;
import com.exmple.task.dto.request.SendMessageByTime;
import com.exmple.task.entity.Task;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class NotificationMailService {

    private final TaskService taskService;
    private final TaskConverter taskConverter;
    private final RestTemplate restTemplate;

    @Value("${mail-service.send-by-time.url}")
    private String sendMailURI;

    public void notifyByTask() {
        Date endDate = new Date(Instant.now().toEpochMilli());
        List<Task> taskList =
                taskService.findTasksByDateAndId(endDate, 0);

        while (!taskList.isEmpty()) {
            for (int i = 0; i < taskList.size(); i++) {
                Task task = taskList.get(i);
                SendMessageByTime message
                        = taskConverter.toSendMessageByTimeDto(task);

                try {
                    ResponseEntity<SendMessageByTime> response =
                            restTemplate.postForEntity(sendMailURI, message, SendMessageByTime.class);
                    if (response.getStatusCode() == HttpStatus.OK) {
                        taskService.deleteById(task.getId());
                    }
                } catch (Exception e) {
                    return;
                }
                if (taskList.size() == i + 1) {
                    taskList = taskService.findTasksByDateAndId(endDate, task.getId());
                }
            }
        }
    }
}
