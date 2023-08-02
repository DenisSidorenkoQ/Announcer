package com.exmple.task.controller;

import com.exmple.task.converter.TaskConverter;
import com.exmple.task.dto.request.task.*;
import com.exmple.task.dto.response.task.TaskResponse;
import com.exmple.task.entity.Task;
import com.exmple.task.entity.User;
import com.exmple.task.service.TaskService;
import java.util.Date;
import java.util.List;
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
    private final TaskConverter taskConverter;

    @GetMapping
    public List<TaskResponse> getTasksByMail(@RequestParam("mail") final String mail) {
        List<Task> taskList = taskService.getTaskByMail(mail);

        return taskList.stream()
                .map(taskConverter::toTaskResponseDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Long> addTask(@RequestBody @Valid final InsertTaskRequest request) {
        Task taskForSave = taskConverter.fromDto(request);
        taskForSave.setAuthor(User.builder().mail(request.getMail()).build());
        long savedTaskId = taskService.createTask(taskForSave);
        return new ResponseEntity<>(savedTaskId, HttpStatus.CREATED);
    }

    @PutMapping("{taskId}/text")
    public ResponseEntity<?> updateTaskText(@PathVariable final int taskId,
                                            @RequestBody final UpdateTaskTextRequest request) {
        taskService.updateTaskText(taskId, request.getText());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("{taskId}/title")
    public ResponseEntity<?> updateTaskTitle(@PathVariable final int taskId,
                                            @RequestBody final UpdateTaskTitleRequest request) {
        taskService.updateTaskTitle(taskId, request.getTitle());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("{taskId}/time")
    public ResponseEntity<?> updateTaskTime(@PathVariable final int taskId,
                                            @RequestBody final UpdateTaskTimeRequest request) {
        taskService.updateTaskTime(taskId, new Date(request.getTimestamp()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<?> deleteTaskById(@PathVariable final int taskId) {
        taskService.deleteById(taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
