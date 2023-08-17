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
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class TaskService extends BaseDomainService<Task> implements ITaskService {

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

    @Override
    public Task create(Task task) {
        throw new NotImplementedException();
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

    @Transactional
    public Task updateText(final Task task) {

        // validation
        Assert.notNull(task, "Task must be specified");
        Assert.notNull(task.getId(), "Task id must be specified");
        Assert.notNull(task.getText(), "Task text must be specified");

        // business logic
        // extract data from DB
        // update ...


        throw new NotImplementedException();
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
        Task persistedTask = get(taskId);
        persistedTask.setStatus(status);
    }


    @Override
    @Transactional
    public void markAsInProgress(long taskId) {
        Task persistedTask = get(taskId);

        if (persistedTask.getStatus() == TaskStatus.ACTIVE) {
            persistedTask.setStatus(TaskStatus.PROGRESS);
            persistedTask.setLastUpdatedTS(LocalDateTime.now());
            persistedTask.setLastUpdatedBy("currentUser");
        } else {
            throw new IllegalStateException("Task cannot be marked as In_Progress");
        }
    }

    @Override
    public void markAsInactive(long taskId) {
        throw new NotImplementedException();
    }

    @Override
    public void markAsRetry(long taskId) {
        throw new NotImplementedException();
    }

    @Override
    @Transactional
    public Task updateMasterData(Task task) {
        // validation
        validateTaskIdNotNull(task);
        Assert.notNull(task.getText(), "Task text must be specified");
        Assert.notNull(task.getTitle(), "Task text must be specified");

        // update data
        Task persistedTask = get(task.getId());
        persistedTask.setTitle(task.getTitle());
        persistedTask.setText(task.getText());
        return persistedTask;
    }

    private void validateTaskIdNotNull(Task task) {
        Assert.notNull(task, "Task must be specified");
        Assert.notNull(task.getId(), "Task id must be specified");
    }

    private Task get(long taskId) {
        Optional<Task> foundTask = taskRepository.findById(taskId);
        return foundTask.orElseThrow(() -> new EntityNotFoundException(ErrorMessages.TASK_NOT_FOUND));
    }


}
