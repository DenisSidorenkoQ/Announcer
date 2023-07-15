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

    // TODO: хард код параметров - нужно это выносить.
    // TODO: этот класс имеет 2 ответственности: 1) конфигурация шедулера, 2) выполнение логики по отправке. Нужно разбивать на 2 класса.
    @Scheduled(fixedDelay = 30000)
    public void sendScheduledTasks() {
        Date startDate = new Date(Instant.now().toEpochMilli() - 30000);
        Date endDate = new Date(Instant.now().toEpochMilli() + 30000);
        // TODO: не очень хорошо выгребать все задачи на заданный промежук. Тут 4 проблемы:
        //  1) мы можем пропустить чего, если приложение долго не работало
        //  2) нам могут набросать очень большое кол-во задач в базу и мы можем повиснуть
        //  3) если запустить несколько инстансов приложений, они будут друг другу гадить
        //  4) Retry механизм нужно пересматривать.
        //  Подумай над всеми этими ньюансами.
        List<Task> taskList = taskService.findAllByDate(startDate, endDate);

        // TODO: каждый раз создаем новый инстанс. Нужно, чтобы у тебя был синглтон бина рест темплета.
        RestTemplate restTemplate = new RestTemplate();
        taskList.forEach(task -> {
            ResponseEntity<SendMessageByTime> response = restTemplate.postForEntity(sendMailURI, taskConverter.toSendMessageByTimeDto(task), SendMessageByTime.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                taskService.deleteById(task.getId());
            }
        });
    }
}
