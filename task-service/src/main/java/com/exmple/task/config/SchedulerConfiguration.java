package com.exmple.task.config;

import com.exmple.task.service.TaskScheduledService;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
public class SchedulerConfiguration {
    private final TaskScheduledService taskScheduledService;

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(dataSource);
    }

    @Scheduled(fixedDelayString = "${scheduler.tasks-time}")
    @SchedulerLock(name = "TaskScheduler_sendScheduledTask")
    public void sendActiveScheduledTask() {
        taskScheduledService.notifyByTask();
    }
}
