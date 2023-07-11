package com.exmple.task.integration.controller;


import com.exmple.task.entity.Task;
import com.exmple.task.integration.config.TestConfigIT;
import com.exmple.task.repository.TaskRepository;
import java.util.Date;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TestConfigIT.class,
        properties = {
                "spring.flyway.enabled=true",
                "spring.flyway.url=jdbc:postgresql://localhost:5432/test",
                "spring.datasource.username=test",
                "spring.datasource.password=test"
        })
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskRepository taskRepository;

    @BeforeAll
    void init() {
        taskRepository.save(Task.builder().id(1).mail("test@gmail.com").title("Title").text("Text").time(new Date(0)).build());
        taskRepository.save(Task.builder().id(2).mail("test@gmail.com").title("Title").text("Text").time(new Date(0)).build());
        taskRepository.save(Task.builder().id(3).mail("testmail@gmail.com").title("Title").text("Text").time(new Date(0)).build());
        taskRepository.save(Task.builder().id(4).mail("testmail@gmail.com").title("Title").text("Text").time(new Date(0)).build());
    }

    @Test
    public void createNewTaskShouldReturnCreatedTaskId() throws Exception {
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mail\":\"test@gmail.com\",\"text\":\"Text\",\"title\":\"Title\",\"timestamp\":1000}"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    public void getTasksByMailShouldReturnTaskList() throws Exception {
        mockMvc.perform(get("/api/v1/tasks")
                .param("mail", "testmail@gmail.com")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":3,\"mail\":\"testmail@gmail.com\",\"title\":\"Title\",\"text\":\"Text\",\"time\":0},{\"id\":4,\"mail\":\"testmail@gmail.com\",\"title\":\"Title\",\"text\":\"Text\",\"time\":0}]"));
    }

    @Test
    public void deleteTaskByIdShouldReturnOK() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/{taskId}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteTaskByIdShouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/{taskId}", 6))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateTaskByIdShouldReturnOK() throws Exception {
        mockMvc.perform(put("/api/v1/tasks/{taskId}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mail\":\"testmail@gmail.com\",\"title\":\"Title\",\"text\":\"Text\",\"timestamp\":0}"))
                .andExpect(status().isOk());
    }

    @Test
    public void updateTaskByIdShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/api/v1/tasks/{taskId}", 6)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mail\":\"testmail@gmail.com\",\"title\":\"Title\",\"text\":\"Text\",\"timestamp\":1000}"))
                .andExpect(status().isBadRequest());
    }
}
