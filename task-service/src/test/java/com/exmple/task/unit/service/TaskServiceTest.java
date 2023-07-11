package com.exmple.task.unit.service;

import com.exmple.task.entity.Task;
import com.exmple.task.repository.TaskRepository;
import com.exmple.task.service.TaskService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskService taskService;

    @Test
    public void shouldCreateTask() {
        Task task = Task.builder()
                .mail("test@gmail.com")
                .text("Text")
                .time(new Date(20000))
                .build();
        Task returnableTask = Task.builder()
                .id(1)
                .mail("test@gmail.com").text("Text").time(new Date(20000)).build();
        given(taskRepository.save(task)).willReturn(returnableTask);

        long taskId = taskService.addTask(task);

        assertThat(taskId, is(1L));
        verify(taskRepository, times(1)).save(task);
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

    @Test
    public void testUpdateTaskWhenExists() {
        Task task = Task.builder().id(1).build();
        when(taskRepository.existsById(task.getId())).thenReturn(true);

        boolean result = taskService.updateTask(task);

        assertTrue(result);
        verify(taskRepository).updateTask(task.getId(), task.getMail(), task.getText(), task.getTitle(), task.getTime());
    }

    @Test
    public void testUpdateTaskWhenNotExists() {
        Task task = Task.builder().id(1).build();
        when(taskRepository.existsById(task.getId())).thenReturn(false);

        boolean result = taskService.updateTask(task);

        assertFalse(result);
        verify(taskRepository, never()).updateTask(anyInt(), anyString(), anyString(), anyString(), any(Date.class));
    }

    @Test
    public void testUpdateTaskWhenException() {
        Task task = Task.builder()
                .id(1)
                .mail("test@gmail.com")
                .text("Text")
                .title("Title")
                .time(new Date())
                .build();
        when(taskRepository.existsById(task.getId())).thenReturn(true);
        doThrow(new RuntimeException()).when(taskRepository).updateTask(task.getId(), task.getMail(), task.getText(), task.getTitle(), task.getTime());

        boolean result = taskService.updateTask(task);

        verify(taskRepository).updateTask(task.getId(), task.getMail(), task.getText(), task.getTitle(), task.getTime());
        assertFalse(result);
    }

    @Test
    public void testDeleteByIdWhenDeleted() {
        long id = 1L;
        doNothing().when(taskRepository).deleteById(id);

        boolean result = taskService.deleteById(id);

        assertTrue(result);
        verify(taskRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteByIdWhenException() {
        long id = 1L;

        doThrow(new RuntimeException()).when(taskRepository).deleteById(id);
        boolean result = taskService.deleteById(id);

        assertFalse(result);
        verify(taskRepository, times(1)).deleteById(id);
    }
}
