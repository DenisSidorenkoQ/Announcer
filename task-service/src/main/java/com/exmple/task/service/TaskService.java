package com.exmple.task.service;

import com.exmple.task.exception.ErrorMessages;
import com.exmple.task.entity.EStatus;
import com.exmple.task.entity.Task;
import com.exmple.task.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.Date;
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

//        Task taskForCreate = Task.builder()
//                .text(task.getText())
//                .title(task.getTitle())
//                .createTS(LocalDateTime.now())
//                .build();

        return taskRepository.save(task).getId();
    }

    @Transactional
    public void updateTaskTextById(final long taskId, final String text) {
        Optional<Task> foundTask = taskRepository.findById(taskId);
        if (foundTask.isPresent()) {
            // TODO (vm): we need business validation here, e.g. check status of the Task

            Task task = foundTask.get();
            task.setText(text);
            // TODO (vm): почему здесь не обязательно вызывать saveAndFlush?
            // taskRepository.saveAndFlush(task);
        } else {
            throw new EntityNotFoundException(ErrorMessages.TASK_NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    public List<Task> getTaskByMail(String mail) {
        return taskRepository.findTaskByAuthor_Mail(mail);
    }

    public void deleteById(long id) {
        Optional<Task> foundTask = taskRepository.findById(id);
        if(foundTask.isPresent()) {
            taskRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(ErrorMessages.TASK_NOT_FOUND);
        }
    }

    public List<Task> findActiveTasksByDateAndId(final Date startDate, final long id) {
        return taskRepository.findActiveTasksByDateAndId(startDate, id, PageRequest.of(0, findTasksLimit));
    }

    public void updateTaskTitleById(final long taskId, final String title) {
        Optional<Task> foundTask = taskRepository.findById(taskId);

        if (foundTask.isPresent()) {
            Task task = foundTask.get();
            task.setTitle(title);
            taskRepository.saveAndFlush(task);
        } else {
            throw new EntityNotFoundException(ErrorMessages.TASK_NOT_FOUND);
        }
    }

    public void updateTaskTimeById(final long taskId, final Date time) {
        Optional<Task> foundTask = taskRepository.findById(taskId);

        if (foundTask.isPresent()) {
            Task task = foundTask.get();
            task.setTime(time);
            taskRepository.saveAndFlush(task);
        } else {
            throw new EntityNotFoundException(ErrorMessages.TASK_NOT_FOUND);
        }

    }

    public void updateTaskStatus(final long taskId, final EStatus status) {
        Optional<Task> foundTask = taskRepository.findById(taskId);

        if (foundTask.isPresent()) {
            Task task = foundTask.get();
            task.setStatus(status.toString());
            taskRepository.saveAndFlush(task);
        } else {
            throw new EntityNotFoundException(ErrorMessages.TASK_NOT_FOUND);
        }
    }
}
