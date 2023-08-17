package com.exmple.task.controller;

import com.exmple.task.converter.TaskConverter;
import com.exmple.task.dto.request.task.*;
import com.exmple.task.dto.response.task.TaskResponse;
import com.exmple.task.entity.Task;
import com.exmple.task.service.TaskService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TaskConverter taskConverter;

    @GetMapping("/users/{userMail}/tasks")
    public List<TaskResponse> getTasksByMail(@PathVariable String userMail) {
        List<Task> taskList = taskService.getTaskByMail(userMail);

        return taskList.stream()
                .map(taskConverter::toTaskResponseDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/users/{userMail}/tasks")
    public ResponseEntity<Long> addTask(@PathVariable String userMail,
                                        @RequestBody final InsertTaskRequest request) {
        Task taskForSave = taskConverter.fromDto(request, userMail);
        // TODO (vm): we need ENUM instead
        // TODO (vm): set status in service layer
        long savedTaskId = taskService.createTask(taskForSave);
        return new ResponseEntity<>(savedTaskId, HttpStatus.CREATED);
    }

    @PutMapping("/tasks/{taskId}/text")
    public void updateTaskTextById(@PathVariable final int taskId,
                                   @RequestBody final UpdateTaskTextRequest request) {
        taskService.updateTaskTextById(taskId, request.getText());
    }

    @PutMapping("/tasks/{taskId}/title")
    public void updateTaskTitleById(@PathVariable final int taskId,
                                    @RequestBody final UpdateTaskTitleRequest request) {
        taskService.updateTaskTitleById(taskId, request.getTitle());
    }

    @PutMapping("/tasks/{taskId}/time")
    public void updateTaskTimeById(@PathVariable final int taskId,
                                   @RequestBody final UpdateTaskTimeRequest request) {
        LocalDateTime time =
                Instant.ofEpochMilli(request.getTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        taskService.updateTaskTimeById(taskId, time);
    }

    @DeleteMapping("/tasks/{taskId}")
    public void deleteTaskById(@PathVariable final int taskId) {
        taskService.deleteById(taskId);
    }

    // TODO (vm): We MUST create signle service method to update all data in SINGLE transaction
    @PutMapping("/tasks/{taskId}/master-info")
    public void updateTaskTitleAndTextById(@PathVariable final int taskId,
                                           @RequestBody final UpdateMasterInfoRequest request) {
        taskService.updateTaskTitleById(taskId, request.getTitle());
        taskService.updateTaskTextById(taskId, request.getText());
    }
}
