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
        boolean taskIsExists = taskRepository.existsById(task.getId());
        if(taskIsExists) {
            try {
                taskRepository.updateTask(task.getId(), task.getMail(), task.getText(), task.getTime());
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public List<Task> getAllByDate(final LocalDate date) {
        Date startOfDay = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endOfDay = Date.from(date.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
        return taskRepository.findAllByDate(startOfDay, endOfDay);
    }

    public List<Task> getTaskByMail(String mail) {
        return taskRepository.findAllByMail(mail);
    }
}
