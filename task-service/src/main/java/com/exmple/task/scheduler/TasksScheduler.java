package com.exmple.task.scheduler;

import com.exmple.task.service.NotificationMailService;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TasksScheduler {

    private final NotificationMailService notificationService;

    @Scheduled(fixedDelayString = "${scheduler.tasks-time}")
    @SchedulerLock(name = "TaskScheduler_sendScheduledTask")
    public void sendScheduledTasks() {
        notificationService.notifyByTask();
    }

    @PostConstruct
    public void sendOldScheduledTasks() {
        notificationService.notifyByTask();
    }
}
