package com.exmple.task.service;

import com.exmple.task.entity.Task;
import com.exmple.task.repository.TaskRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void shouldCreateTask() {
        Task task = Task.builder().mail("test@gmail.com").text("Text").time(new Date(20000)).build();
        Task returnableTask = Task.builder().id(1).mail("test@gmail.com").text("Text").time(new Date(20000)).build();
        given(taskRepository.save(task)).willReturn(returnableTask);

        long taskId = taskService.addTask(task);
        assertThat(taskId, is(1L));
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void shouldUpdateExistsTask() {
        Task task = new Task(1, "test@gmail.com", "Text", new Date(20000));
        when(taskRepository.existsById(task.getId())).thenReturn(true);
        doNothing().when(taskRepository).updateTask(task.getId(), task.getMail(), task.getText(), task.getTime());
        boolean result = taskService.updateTask(task);
        assertTrue(result);
        verify(taskRepository, times(1)).existsById(task.getId());
        verify(taskRepository, times(1)).updateTask(task.getId(), task.getMail(), task.getText(), task.getTime());
    }

    @Test
    public void shouldNotUpdateTaskAndThrowException() {
        Task task = new Task();
        task.setId(1);
        task.setMail("test@example.com");
        task.setText("Test task");
        task.setTime(new Date(1000));
        when(taskRepository.existsById(task.getId())).thenReturn(true);
        doThrow(new RuntimeException()).when(taskRepository).updateTask(task.getId(), task.getMail(), task.getText(), task.getTime());
        boolean result = taskService.updateTask(task);
        assertFalse(result);
        verify(taskRepository, times(1)).existsById(task.getId());
        verify(taskRepository, times(1)).updateTask(task.getId(), task.getMail(), task.getText(), task.getTime());
    }

    @Test
    public void shouldNotUpdateTaskWhenNotExists() {
        Task task = new Task(1, "test@gmail.com", "Text", new Date(20000));
        when(taskRepository.existsById(task.getId())).thenReturn(false);
        boolean result = taskService.updateTask(task);
        assertFalse(result);
        verify(taskRepository, times(1)).existsById(task.getId());
        verify(taskRepository, never()).updateTask(task.getId(), task.getMail(), task.getText(), task.getTime());
    }

    @Test
    public void shouldGetTaskByMail() {
        String mail = "test@gmail.com";
        List<Task> tasks = new ArrayList<>();
        tasks.add(Task.builder().mail("test@gmail.com").build());
        tasks.add(Task.builder().mail("test@gmail.com").build());
        when(taskRepository.findAllByMail(mail)).thenReturn(tasks);
        List<Task> result = taskService.getTaskByMail(mail);
        Assertions.assertEquals(tasks, result);
        verify(taskRepository, times(1)).findAllByMail(mail);
    }
}
