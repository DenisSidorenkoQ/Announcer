package com.exmple.task.integration.controller;

import com.exmple.task.dto.request.user.InsertUserRequest;
import com.exmple.task.entity.Task;
import com.exmple.task.entity.User;
import com.exmple.task.integration.config.TestConfigIT;
import com.exmple.task.repository.TaskRepository;
import com.exmple.task.repository.UserRepository;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
        userRepository.save(User.builder().id(1).name("TestUser").mail("test@gmail.com").build());
        taskRepository.save(Task.builder().title("Title").author(User.builder().id(1).name("TestUser").mail("test@gmail.com").build()).text("Text").time(new Date(0)).build());
        taskRepository.save(Task.builder().title("Title").author(User.builder().id(1).name("TestUser").mail("test@gmail.com").build()).text("Text").time(new Date(0)).build());
    }

    @Test
    public void createNewUserShouldReturnCreatedUserId() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"TestUser\",\"mail\":\"test2@gmail.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("2"));
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
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("mail", "test@gmail.com")
                        .content(""))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"TestUser\",\"mail\":\"test@gmail.com\"}"));
    }

    @Test
    public void getUserShouldReturnNotFoundWhenUserNotExists() throws Exception {
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("mail", "test3@gmail.com")
                        .content(""))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUserWithTasksByMailShouldReturnOkWhenUserIsExists() throws Exception {
        mockMvc.perform(get("/api/v1/users/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("mail", "test@gmail.com")
                        .content(""))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"TestUser\",\"mail\":\"test@gmail.com\",\"tasks\":[{\"id\":1,\"title\":\"Title\",\"text\":\"Text\",\"time\":\"1970-01-01T00:00:00.000+00:00\"}," +
                        "{\"id\":2,\"title\":\"Title\",\"text\":\"Text\",\"time\":\"1970-01-01T00:00:00.000+00:00\"}]}"));
    }

    @Test
    public void getUserWithTasksByMailShouldReturnNotFoundWhenUserNotExists() throws Exception {
        mockMvc.perform(get("/api/v1/users/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("mail", "test3@gmail.com")
                        .content(""))
                .andExpect(status().isNotFound());
    }
}
