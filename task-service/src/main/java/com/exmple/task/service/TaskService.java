package com.exmple.task.service;

import com.exmple.task.entity.Task;
import com.exmple.task.repository.TaskRepository;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    @Value("${tasks.find-tasks.limit}")
    private int findTasksLimit;

    public long addTask(Task task) {
        return taskRepository.save(task).getId();
    }

    @Transactional
    public boolean updateTask(Task task) {
        boolean taskExists = taskRepository.existsById(task.getId());
        if (taskExists) {
            try {
                taskRepository.updateTask(task.getId(), task.getText(), task.getTitle(), task.getTime());
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public List<Task> getTaskByMail(String mail) {
        return taskRepository.findTaskByAuthor_Mail(mail);
    }

    public boolean deleteById(long id) {
        try {
            taskRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<Task> findTasksByDateAndId(final Date startDate, final long id) {
        return taskRepository.findTasksByDateAndId(startDate, id, PageRequest.of(0, findTasksLimit));
    }
}
