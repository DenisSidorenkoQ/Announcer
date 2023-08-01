package com.exmple.task.controller;

import com.exmple.task.converter.TaskConverter;
import com.exmple.task.dto.request.task.InsertTaskRequest;
import com.exmple.task.dto.request.task.UpdateTaskRequest;
import com.exmple.task.dto.response.task.TaskResponse;
import com.exmple.task.entity.Task;
import com.exmple.task.entity.User;
import com.exmple.task.service.TaskService;
import com.exmple.task.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final TaskConverter taskConverter;

    @GetMapping
    public List<TaskResponse> getTasksByMail(@RequestParam("mail") final String mail) {
        List<Task> taskList = taskService.getTaskByMail(mail);

        return taskList.stream()
                .map(taskConverter::toTaskResponseDto).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Long> addTask(@RequestBody @Valid final InsertTaskRequest request) {
        Task taskForSave = taskConverter.fromDto(request);
        Optional<User> user = userService.getUserByMail(request.getMail());

        if(user.isPresent()) {
            taskForSave.setAuthor(user.get());
            long savedTaskId = taskService.addTask(taskForSave);
            return new ResponseEntity<>(savedTaskId, HttpStatus.CREATED);
        }
       return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable final int taskId,
                                        @RequestBody @Valid final UpdateTaskRequest request) {
        Task taskForUpdate = taskConverter.fromDto(request, taskId);
        boolean taskIsUpdated = taskService.updateTask(taskForUpdate);

        return taskIsUpdated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<?> deleteTaskById(@PathVariable final int taskId) {
        boolean taskIsDelete = taskService.deleteById(taskId);
        if(taskIsDelete) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
