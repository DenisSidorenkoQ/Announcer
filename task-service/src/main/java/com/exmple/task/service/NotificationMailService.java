package com.exmple.task.service;

import com.exmple.task.converter.TaskConverter;
import com.exmple.task.dto.request.SendMessageByTime;
import com.exmple.task.entity.EStatus;
import com.exmple.task.entity.Task;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationMailService {

    private final TaskService taskService;
    private final TaskConverter taskConverter;
    private final RestTemplate restTemplate;

    @Value("${mail-service.send-by-time.url}")
    private String sendMailURI;

    @Value("${tasks.find-tasks.limit}")
    private int findTasksLimit;

    public void notifyByTask() {
        Date endDate = new Date(Instant.now().toEpochMilli());
        List<Task> taskList =
                taskService.findActiveTasksByDateAndId(endDate, 0);

        while (!taskList.isEmpty()) {
            for (int i = 0; i < taskList.size(); i++) {
                final Task task = taskList.get(i);
                try {
                    taskService.updateTaskStatus(task.getId(), EStatus.STATUS_INACTIVE);
                    SendMessageByTime message
                            = taskConverter.toSendMessageByTimeDto(task);
                    ResponseEntity<SendMessageByTime> sendMailResponse =
                            restTemplate.postForEntity(sendMailURI, message, SendMessageByTime.class);
                    taskList.remove(i);
                    if (sendMailResponse.getStatusCode() != HttpStatus.OK) {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    log.warn("Message was not sent");
                    taskService.updateTaskStatus(task.getId(), EStatus.STATUS_ACTIVE);
                }
                if (taskList.size() == findTasksLimit) {
                    taskList = taskService.findActiveTasksByDateAndId(endDate, task.getId());
                }
            }
        }
    }
}
