package com.exmple.task.integration.controller;

import com.exmple.task.entity.TaskStatus;
import com.exmple.task.entity.Task;
import com.exmple.task.entity.User;
import com.exmple.task.integration.config.TestConfigIT;
import com.exmple.task.repository.TaskRepository;
import com.exmple.task.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestConfigIT.class)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/application-test.properties")
public class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeAll
    void init() {
        userRepository.save(User.builder().name("TestUser").mail("test@gmail.com").build());
        taskRepository.save(Task.builder().title("Title").userMail("test@gmail.com").text("Text").time(LocalDateTime.now()).status(TaskStatus.ACTIVE).build());
        taskRepository.save(Task.builder().title("Title").userMail("test@gmail.com").text("Text").time(LocalDateTime.now()).status(TaskStatus.ACTIVE).build());
    }

    @Test
    public void createNewUserShouldReturnCreatedUserMail() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"TestUser\",\"mail\":\"test4@gmail.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("test4@gmail.com"));
    }

    @Test
    public void createNewUserShouldReturnConflictWhenUserIsExists() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"TestUser\",\"mail\":\"test@gmail.com\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    public void getUserShouldReturnOKWhenUserIsExists() throws Exception {
        mockMvc.perform(get("/api/v1/users/{userMail}", "test@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"TestUser\",\"mail\":\"test@gmail.com\"}"));
    }

    @Test
    public void getUserShouldReturnNotFoundWhenUserNotExists() throws Exception {
        mockMvc.perform(get("/api/v1/users/{userMail}", "test5@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound());
    }
}
