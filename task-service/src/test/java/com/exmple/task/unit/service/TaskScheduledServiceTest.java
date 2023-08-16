package com.exmple.task.unit.service;

import com.exmple.task.converter.TaskConverter;
import com.exmple.task.dto.request.SendMessageByTime;
import com.exmple.task.entity.TaskStatus;
import com.exmple.task.entity.Task;
import com.exmple.task.service.NotificationService;
import com.exmple.task.service.RestNotificationService;
import com.exmple.task.service.TaskScheduledService;
import com.exmple.task.service.TaskService;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TaskScheduledServiceTest {
    @Mock
    private TaskService taskService;
    @Mock
    private TaskConverter taskConverter;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private TaskScheduledService taskScheduledService;

    @Test
    public void testNotifyByTaskWhenMessageNotSent() throws Exception {
        Task task = Task.builder().id(1).status(TaskStatus.ACTIVE).time(LocalDateTime.now()).build();
        List<Task> taskList = new ArrayList<>();
        taskList.add(task);
        when(taskService.findTasksByDateAndId(any(LocalDateTime.class), anyLong())).thenReturn(taskList).thenReturn(new ArrayList<>());
        when(taskConverter.toSendMessageByTimeDto(any(Task.class))).thenReturn(SendMessageByTime.builder().build());
        doThrow(RuntimeException.class).when(notificationService).sendTaskNotification(any(SendMessageByTime.class));

        taskScheduledService.notifyByTask();

        verify(taskService, times(1)).updateTaskStatus(eq(task.getId()), eq(TaskStatus.PROGRESS));
        verify(taskService, times(1)).updateTaskStatus(eq(task.getId()), eq(TaskStatus.RETRY));
    }

    @Test
    public void testNotifyByTaskWhenMessageSent() {
        Task task = Task.builder().id(1).status(TaskStatus.ACTIVE).time(LocalDateTime.now()).build();
        List<Task> taskList = new ArrayList<>();
        taskList.add(task);
        when(taskService.findTasksByDateAndId(any(LocalDateTime.class), anyLong())).thenReturn(taskList).thenReturn(new ArrayList<>());
        when(taskConverter.toSendMessageByTimeDto(any(Task.class))).thenReturn(SendMessageByTime.builder().build());

        taskScheduledService.notifyByTask();

        verify(taskService, never()).updateTaskStatus(eq(task.getId()), eq(TaskStatus.RETRY));
        verify(taskService, times(1)).updateTaskStatus(eq(task.getId()), eq(TaskStatus.PROGRESS));
        verify(taskService, times(1)).updateTaskStatus(eq(task.getId()), eq(TaskStatus.INACTIVE));
    }
}
