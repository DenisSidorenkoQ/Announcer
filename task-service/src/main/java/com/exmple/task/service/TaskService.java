package com.exmple.task.service;

import com.exmple.task.entity.Task;
import com.exmple.task.repository.TaskRepository;
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
        return taskRepository.save(task).getId();
    }

    @Transactional
    public void updateTaskText(final long taskId, final String text) {
        Optional<Task> foundTask = taskRepository.findById(taskId);
        if (foundTask.isPresent()) {
            Task task = foundTask.get();
            task.setText(text);
            taskRepository.saveAndFlush(task);
        } else {
            throw new EntityNotFoundException("Task not found");
        }
    }

    public List<Task> getTaskByMail(String mail) {
        return taskRepository.findTaskByAuthor_Mail(mail);
    }

    public void deleteById(long id) {
        Optional<Task> foundTask = taskRepository.findById(id);
        if(foundTask.isPresent()) {
            taskRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Task not found");
        }
    }

    public List<Task> findTasksByDateAndId(final Date startDate, final long id) {
        return taskRepository.findTasksByDateAndId(startDate, id, PageRequest.of(0, findTasksLimit));
    }

    public void updateTaskTitle(final long taskId, final String title) {
        Optional<Task> foundTask = taskRepository.findById(taskId);

        if (foundTask.isPresent()) {
            Task task = foundTask.get();
            task.setTitle(title);
            taskRepository.saveAndFlush(task);
        } else {
            throw new EntityNotFoundException("Task not found");
        }
    }

    public void updateTaskTime(final long taskId, final Date time) {
        Optional<Task> foundTask = taskRepository.findById(taskId);

        if (foundTask.isPresent()) {
            Task task = foundTask.get();
            task.setTime(time);
            taskRepository.saveAndFlush(task);
        } else {
            throw new EntityNotFoundException("Task not found");
        }

    }
}
