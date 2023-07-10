package com.exmple.task.controller;

import com.exmple.task.converter.TaskConverter;
import com.exmple.task.dto.UpsertTaskRequest;
import com.exmple.task.entity.Task;
import com.exmple.task.service.TaskService;
import java.util.List;
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
    public List<Task> getTasksByMail(@RequestParam("mail") String mail) {
        return taskService.getTaskByMail(mail);
    }

    @PostMapping
    public long addTask(@RequestBody @Valid UpsertTaskRequest request) {
        Task taskForSave = taskConverter.fromDto(request);
        return taskService.addTask(taskForSave);
    }

    @PutMapping("{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable int taskId, @RequestBody @Valid UpsertTaskRequest request) {
        Task taskForUpdate = taskConverter.fromDto(request, taskId);
        boolean taskIsUpdated = taskService.updateTask(taskForUpdate);
        if(taskIsUpdated) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}