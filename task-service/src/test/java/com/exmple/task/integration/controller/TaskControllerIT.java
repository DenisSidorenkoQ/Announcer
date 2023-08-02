package com.exmple.task.integration.controller;


import com.exmple.task.entity.Task;
import com.exmple.task.entity.User;
import com.exmple.task.integration.config.TestConfigIT;
import com.exmple.task.repository.TaskRepository;
import com.exmple.task.repository.UserRepository;
import java.util.Date;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TestConfigIT.class)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/application-test.properties")
public class TaskControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void init() {
        userRepository.save(User.builder().mail("test@gmail.com").name("TestUser").build());
        userRepository.save(User.builder().mail("test2@gmail.com").name("TestUser").build());
        taskRepository.save(Task.builder().id(1).title("Title").author(User.builder().mail("test@gmail.com").build()).text("Text").time(new Date(0)).build());
        taskRepository.save(Task.builder().id(2).title("Title").author(User.builder().mail("test@gmail.com").build()).text("Text").time(new Date(0)).build());
        taskRepository.save(Task.builder().id(3).title("Title").author(User.builder().name("TestUser").mail("test@gmail.com").build()).text("Text").time(new Date(0)).build());
        taskRepository.save(Task.builder().id(4).title("Title").author(User.builder().name("TestUser").mail("test@gmail.com").build()).text("Text").time(new Date(0)).build());
        taskRepository.save(Task.builder().id(5).title("Title").author(User.builder().name("TestUser").mail("test2@gmail.com").build()).text("Text").time(new Date(0)).build());
    }

    @Test
    public void createNewTaskShouldReturnCreatedTaskId() throws Exception {
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mail\":\"test@gmail.com\",\"title\":\"Title\",\"text\":\"Text\",\"timestamp\":1000}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("6"));
    }

    @Test
    public void getTasksByMailShouldReturnTaskList() throws Exception {
        mockMvc.perform(get("/api/v1/tasks")
                .param("mail", "test@gmail.com")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("[" +
                        "{\"id\":2,\"title\":\"Title\",\"text\":\"Text\",\"time\":0}," +
                        "{\"id\":3,\"title\":\"Title\",\"text\":\"Text\",\"time\":0}," +
                        "{\"id\":4,\"title\":\"Title\",\"text\":\"Text\",\"time\":0}" +
                        "]"));
    }

    @Test
    public void deleteTaskByIdShouldReturnOK() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/{taskId}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteTaskByIdShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/{taskId}", 6))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateTaskTextByIdShouldReturnOK() throws Exception {
        mockMvc.perform(put("/api/v1/tasks/{taskId}/text", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Text\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void updateTaskByIdShouldReturnNotFound() throws Exception {
        mockMvc.perform(put("/api/v1/tasks/{taskId}/text", 6)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Text\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateTaskTitleByIdShouldReturnOK() throws Exception {
        mockMvc.perform(put("/api/v1/tasks/{taskId}/title", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Title\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void updateTaskTitleByIdShouldReturnNotFound() throws Exception {
        mockMvc.perform(put("/api/v1/tasks/{taskId}/title", 6)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Title\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateTaskTimeByIdShouldReturnOK() throws Exception {
        mockMvc.perform(put("/api/v1/tasks/{taskId}/time", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"timestamp\":111111111}"))
                .andExpect(status().isOk());
    }

    @Test
    public void updateTaskTimeByIdShouldReturnNotFound() throws Exception {
        mockMvc.perform(put("/api/v1/tasks/{taskId}/time", 6)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"timestamp\":111111111}"))
                .andExpect(status().isNotFound());
    }
}
