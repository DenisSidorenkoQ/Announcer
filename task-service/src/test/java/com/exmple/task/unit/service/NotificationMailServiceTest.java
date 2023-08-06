package com.exmple.task.unit.service;

import com.exmple.task.converter.TaskConverter;
import com.exmple.task.dto.request.SendMessageByTime;
import com.exmple.task.entity.EStatus;
import com.exmple.task.entity.Task;
import com.exmple.task.service.NotificationMailService;
import com.exmple.task.service.TaskService;
import java.lang.reflect.Field;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NotificationMailServiceTest {
    @Mock
    private TaskService taskService;
    @Mock
    private TaskConverter taskConverter;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private NotificationMailService notificationMailService;

    @Before
    public void init() throws NoSuchFieldException, IllegalAccessException {
        Field sendMailURIField = NotificationMailService.class.getDeclaredField("sendMailURI");
        sendMailURIField.setAccessible(true);
        sendMailURIField.set(notificationMailService, "URL");

        Field findTasksLimitField = NotificationMailService.class.getDeclaredField("findTasksLimit");
        findTasksLimitField.setAccessible(true);
        findTasksLimitField.set(notificationMailService, 20);
    }

    @Test
    public void testNotifyByTaskWhenMessageNotSent() {
        Task task = Task.builder().id(1).status(EStatus.STATUS_ACTIVE.toString()).time(new Date()).build();
        List<Task> taskList = new ArrayList<>();
        taskList.add(task);
        when(taskService.findActiveTasksByDateAndId(any(Date.class), anyLong())).thenReturn(taskList);
        when(taskConverter.toSendMessageByTimeDto(any(Task.class))).thenReturn(SendMessageByTime.builder().build());
        when(restTemplate.postForEntity("URL", SendMessageByTime.builder().build(), SendMessageByTime.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        notificationMailService.notifyByTask();

        verify(taskService, times(1)).updateTaskStatus(eq(task.getId()), eq(EStatus.STATUS_INACTIVE));
        verify(taskService, times(1)).updateTaskStatus(eq(task.getId()), eq(EStatus.STATUS_ACTIVE));
    }

    @Test
    public void testNotifyByTaskWhenMessageSent() {
        Task task = Task.builder().id(1).status(EStatus.STATUS_ACTIVE.toString()).time(new Date()).build();
        List<Task> taskList = new ArrayList<>();
        taskList.add(task);
        when(taskService.findActiveTasksByDateAndId(any(Date.class), anyLong())).thenReturn(taskList);
        when(taskConverter.toSendMessageByTimeDto(any(Task.class))).thenReturn(SendMessageByTime.builder().build());
        when(restTemplate.postForEntity("URL", SendMessageByTime.builder().build(), SendMessageByTime.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        notificationMailService.notifyByTask();

        verify(taskService, never()).updateTaskStatus(eq(task.getId()), eq(EStatus.STATUS_ACTIVE));
        verify(taskService, times(1)).updateTaskStatus(eq(task.getId()), eq(EStatus.STATUS_INACTIVE));
    }
}
