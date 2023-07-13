package com.exmple.task.scheduler;

import com.exmple.task.converter.TaskConverter;
import com.exmple.task.entity.Task;
import com.exmple.task.producer.Producer;
import com.exmple.task.service.TaskService;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotifierScheduler {
    private final TaskService taskService;
    private final TaskConverter taskConverter;
    private final Producer producer;
    @Value("${spring.kafka.topic.task.name}")
    private String taskTopic;

    @Scheduled(fixedDelay = 30000)
    public void sendScheduledTasks() {
        Date startDate = new Date(Instant.now().toEpochMilli() - 30000);
        Date endDate = new Date(Instant.now().toEpochMilli() + 30000);
        List<Task> taskList = taskService.findAllByDate(startDate, endDate);

        taskList.forEach(task -> {
            producer.sendMessage(taskTopic, taskConverter.toSendMessageByTimeDto(task));
        });
    }
}
