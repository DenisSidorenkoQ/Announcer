package com.exmple.task.controller;

import com.exmple.task.converter.TaskConverter;
import com.exmple.task.dto.UpsertTaskRequest;
import com.exmple.task.entity.Task;
import com.exmple.task.service.TaskService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaskControllerTest {
    @Mock
    private TaskService taskService;

    @Mock
    private TaskConverter taskConverter;

    @InjectMocks
    private TaskController taskController;

    @Test
    public void shouldGetTaskByMail() {
        String mail = "test@gmail.com";
        List<Task> tasks = new ArrayList<>();
        tasks.add(Task.builder().mail("test@gmail.com").build());
        tasks.add(Task.builder().mail("test@gmail.com").build());

        when(taskService.getTaskByMail(mail)).thenReturn(tasks);
        List<Task> result = taskController.getTasksByMail(mail);
        assertEquals(tasks, result);
        verify(taskService, times(1)).getTaskByMail(mail);
    }

    @Test
    public void shouldAddTask() {
        UpsertTaskRequest taskRequest = new UpsertTaskRequest("test@gmail.com", "Text", 10000);
        Task resultTask = Task.builder().mail("test@gmail.com").text("Text").time(new Date(10000)).build();

        when(taskService.addTask(resultTask)).thenReturn(1L);
        when(taskConverter.fromDto(taskRequest)).thenReturn(resultTask);
        long resultId = taskController.addTask(taskRequest);
        assertEquals(resultId, 1L);
        verify(taskService, times(1)).addTask(resultTask);
    }

    @Test
    public void shouldUpdateTask_Success() {
        int taskId = 1;
        UpsertTaskRequest request = new UpsertTaskRequest();
        Task taskForUpdate = Task.builder().id(taskId).build();

        when(taskService.updateTask(taskForUpdate)).thenReturn(true);
        when(taskConverter.fromDto(request, taskId)).thenReturn(taskForUpdate);
        ResponseEntity<?> response = taskController.updateTask(taskId, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldUpdateTask_Failure() {
        int taskId = 1;
        UpsertTaskRequest request = new UpsertTaskRequest();
        Task taskForUpdate = new Task();
        taskForUpdate.setId(taskId);
        when(taskService.updateTask(taskForUpdate)).thenReturn(false);
        ResponseEntity<?> response = taskController.updateTask(taskId, request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
