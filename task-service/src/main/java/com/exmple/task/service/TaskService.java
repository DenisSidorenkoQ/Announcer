package com.exmple.task.service;

import com.exmple.task.exception.ErrorMessages;
import com.exmple.task.entity.TaskStatus;
import com.exmple.task.entity.Task;
import com.exmple.task.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    @Value("${tasks.find-tasks.limit}")
    private int findTasksLimit;

    public long createTask(Task task) {

        Task taskForCreate = Task.builder()
                .text(task.getText())
                .title(task.getTitle())
                .time(task.getTime())
                .userMail(task.getUserMail())
                .status(TaskStatus.ACTIVE)
                .build();

        return taskRepository.save(taskForCreate).getId();
    }

    @Transactional
    public void updateTaskTextById(final long taskId, final String text) {
        Optional<Task> foundTask = taskRepository.findById(taskId);
        if (foundTask.isPresent()) {
            // TODO (vm): we need business validation here, e.g. check status of the Task
            Task task = foundTask.get();
            if(task.getStatus().equals(TaskStatus.ACTIVE)) {
                task.setText(text);
            }
            // TODO (vm): почему здесь не обязательно вызывать saveAndFlush?
            // taskRepository.saveAndFlush(task);
        } else {
            throw new EntityNotFoundException(ErrorMessages.TASK_NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    public List<Task> getTaskByMail(String mail) {
        return taskRepository.findTasksByUserMail(mail);
    }

    @Transactional
    public void deleteById(long id) {
        Optional<Task> foundTask = taskRepository.findById(id);
        if(foundTask.isPresent()) {
            taskRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(ErrorMessages.TASK_NOT_FOUND);
        }
    }

    public List<Task> findTasksByDateAndId(final LocalDateTime startDate, final long id) {
        return taskRepository.findActiveTasksByDateAndId(startDate, id, PageRequest.of(0, findTasksLimit));
    }

    @Transactional
    public void updateTaskTitleById(final long taskId, final String title) {
        Optional<Task> foundTask = taskRepository.findById(taskId);

        if (foundTask.isPresent()) {
            Task task = foundTask.get();
            if(task.getStatus().equals(TaskStatus.ACTIVE)) {
                task.setTitle(title);
            }
        } else {
            throw new EntityNotFoundException(ErrorMessages.TASK_NOT_FOUND);
        }
    }

    public void updateTaskTimeById(final long taskId, final LocalDateTime time) {
        Optional<Task> foundTask = taskRepository.findById(taskId);

        if (foundTask.isPresent()) {
            Task task = foundTask.get();
            if(task.getStatus().equals(TaskStatus.ACTIVE)) {
                task.setTime(time);
            }
        } else {
            throw new EntityNotFoundException(ErrorMessages.TASK_NOT_FOUND);
        }

    }

    @Transactional
    public void updateTaskStatus(final long taskId, final TaskStatus status) {
        Optional<Task> foundTask = taskRepository.findById(taskId);

        if (foundTask.isPresent()) {
            Task task = foundTask.get();
            task.setStatus(status);
        } else {
            throw new EntityNotFoundException(ErrorMessages.TASK_NOT_FOUND);
        }
    }
}
