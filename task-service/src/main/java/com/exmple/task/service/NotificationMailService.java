package com.exmple.task.service;

import com.exmple.task.exception.ErrorMessages;
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

    // TODO (vm): я бы создал NotificationService, у которого было бы 2 имплементации: RestNotificationService, MessagingNotificationService
    private final RestTemplate restTemplate;

    @Value("${mail-service.send-by-time.url}")
    private String sendMailURI;

    @Value("${tasks.find-tasks.limit}")
    private int findTasksLimit;
    @Value("${time-zone}")
    private int TIME_ZONE;

    public void notifyByTask() {
        Date endDate = new Date(Instant.now().plusSeconds(3600L * TIME_ZONE).toEpochMilli()); //UTC+3
        List<Task> taskList =
                taskService.findActiveTasksByDateAndId(endDate, 0);

        while (!taskList.isEmpty()) {
            for (int i = 0; i < taskList.size(); i++) {
                final Task task = taskList.get(i);
                try {

                    // 1. mark task as in process by current instance (1 transaction)

                    // 2. try to send

                    // 3. analyse sent message and change status to Inactive

                    // 4. for error sending we can mark task for sending retry






                    taskService.updateTaskStatus(task.getId(), EStatus.STATUS_INACTIVE);
                    SendMessageByTime message
                            = taskConverter.toSendMessageByTimeDto(task);
                    ResponseEntity<SendMessageByTime> sendMailResponse =
                            restTemplate.postForEntity(sendMailURI, message, SendMessageByTime.class);
                    if (sendMailResponse.getStatusCode() != HttpStatus.OK) {
                        throw new Exception();
                    } else {
                        taskList.remove(i);
                    }
                } catch (Exception e) {
                    log.warn(ErrorMessages.MESSAGE_NOT_SENT);
                    taskService.updateTaskStatus(task.getId(), EStatus.STATUS_ACTIVE);
                    taskList.remove(i);
                }
                if (taskList.isEmpty()) {
                    taskList = taskService.findActiveTasksByDateAndId(endDate, task.getId());
                }
            }
        }
    }
}
