package com.exmple.task.service;

import com.exmple.task.entity.Task;
import com.exmple.task.repository.TaskRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public long addTask(Task task) {
        return taskRepository.save(task).getId();
    }

    @Transactional
    public boolean updateTask(Task task) {
        boolean taskExists = taskRepository.existsById(task.getId());
        if (taskExists) {
            try {
                taskRepository
                        .updateTask(task.getId(), task.getMail(), task.getText(), task.getTitle(), task.getTime());
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public List<Task> getTaskByMail(String mail) {
        return taskRepository.findAllByMail(mail);
    }

    public List<Task> findAllByDate(Date startDate, Date endDate) {
        return taskRepository.findAllByDate(startDate, endDate);
    }

    public boolean deleteById(long id) {
        try {
            taskRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
