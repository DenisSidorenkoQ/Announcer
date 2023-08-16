package com.exmple.task.service;

import com.exmple.task.exception.ErrorMessages;
import com.exmple.task.converter.TaskConverter;
import com.exmple.task.dto.request.SendMessageByTime;
import com.exmple.task.entity.TaskStatus;
import com.exmple.task.entity.Task;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskScheduledService {

    private final TaskService taskService;
    private final TaskConverter taskConverter;

    // TODO (vm): я бы создал NotificationService, у которого было бы 2 имплементации: RestNotificationService, MessagingNotificationService
    private final NotificationService notificationService;

    @Transactional
    public void notifyByTask() {
        LocalDateTime endDate = LocalDateTime.now();
        List<Task> taskList =
                taskService.findTasksByDateAndId(endDate, 0);

        while (!taskList.isEmpty()) {
            for (int i = 0; i < taskList.size(); i++) {
                final Task task = taskList.get(i);
                try {
                    // 1. mark task as in process by current instance (1 transaction)

                    // 2. try to send

                    // 3. analyse sent message and change status to Inactive

                    // 4. for error sending we can mark task for sending retry


                    taskService.updateTaskStatus(task.getId(), TaskStatus.PROGRESS);
                    SendMessageByTime message
                            = taskConverter.toSendMessageByTimeDto(task);
                    notificationService.sendTaskNotification(message);
                    taskService.updateTaskStatus(task.getId(), TaskStatus.INACTIVE);
                } catch (Exception e) {
                    log.warn(ErrorMessages.MESSAGE_NOT_SENT);
                    taskService.updateTaskStatus(task.getId(), TaskStatus.RETRY);
                }
                if (taskList.size() == i + 1) {
                    taskList = taskService.findTasksByDateAndId(endDate, task.getId());
                }
            }
        }
    }

}
