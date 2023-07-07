package com.exmple.task.scheduler;

import com.exmple.task.entity.Task;
import com.exmple.task.service.TaskService;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotifierScheduler {
    private final TaskService taskService;

    @Scheduled(fixedRate = 3600000)
    public void sendTask() {
        List<Task> taskList = taskService.getAllByDate(LocalDate.now());
    }
}
