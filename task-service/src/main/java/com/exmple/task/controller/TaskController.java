package com.exmple.task.controller;

import com.exmple.task.converter.TaskConverter;
import com.exmple.task.dto.request.UpsertTaskRequest;
import com.exmple.task.dto.response.TaskResponse;
import com.exmple.task.entity.Task;
import com.exmple.task.service.TaskService;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: предлагаю добавить еще одну сущность, User, которому назначены таски. Как раз поиграемся с более сложными зависимостями.
//  Поэкспериментируй с бизнес логикой - какие были бы полезны ендпоинты?


@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TaskConverter taskConverter;

    @GetMapping
    public List<TaskResponse> getTasksByMail(@RequestParam("mail") final String mail) {
        List<Task> taskList = taskService.getTaskByMail(mail);

        // TODO: тут можно сделать более изящно через Stream API. Давай подумаем как это сделать?
        List<TaskResponse> taskResponseList = new ArrayList<>();
        taskList.forEach(task -> {
            taskResponseList.add(taskConverter.toTaskResponseDto(task));
        });

        return taskResponseList;
    }

    @PostMapping
    public long addTask(@RequestBody @Valid final UpsertTaskRequest request) {
        Task taskForSave = taskConverter.fromDto(request);
        return taskService.addTask(taskForSave);
    }

    // TODO: метод по обновлению таски. А все ли данные в таске мы можем обновлять? Это вопрос больше к бизнес логике.
    //  Сам метод updateTask получается не очень хорошо определяет свою ответственность.
    @PutMapping("{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable final int taskId, @RequestBody @Valid final UpsertTaskRequest request) {
        Task taskForUpdate = taskConverter.fromDto(request, taskId);
        boolean taskIsUpdated = taskService.updateTask(taskForUpdate);


        // TODO: как более короче записать такую конструкцию?
        if(taskIsUpdated) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // TODO: почему тут BAD_REQUEST?
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<?> deleteTaskById(@PathVariable final int taskId) {
        boolean taskIsDelete = taskService.deleteById(taskId);
        if(taskIsDelete) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
