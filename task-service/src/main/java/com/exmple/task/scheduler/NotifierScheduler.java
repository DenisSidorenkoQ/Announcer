package com.exmple.task.scheduler;

import com.exmple.task.converter.TaskConverter;
import com.exmple.task.dto.request.SendMessageByTime;
import com.exmple.task.entity.Task;
import com.exmple.task.service.TaskService;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class NotifierScheduler {
    private final TaskService taskService;
    private final TaskConverter taskConverter;
    @Value("${mail-service.send-by-time.url}")
    private String sendMailURI;

    @Scheduled(fixedDelay = 30000)
    public void sendScheduledTasks() {
        Date startDate = new Date(Instant.now().toEpochMilli() - 30000);
        Date endDate = new Date(Instant.now().toEpochMilli() + 30000);
        List<Task> taskList = taskService.findAllByDate(startDate, endDate);

        RestTemplate restTemplate = new RestTemplate();
        taskList.forEach(task -> {
            ResponseEntity<SendMessageByTime> response = restTemplate.postForEntity(sendMailURI, taskConverter.toDto(task), SendMessageByTime.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                taskService.deleteById(task.getId());
            }
        });
    }
}
