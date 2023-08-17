package com.exmple.task.service;

import com.exmple.task.entity.Task;

public interface ITaskService extends IBaseDomainService<Task> {
    Task updateText(Task task);

    Task updateMasterData(Task task);


    void markAsInProgress(long taskId);
    void markAsInactive(long taskId);
    void markAsRetry(long taskId);
}
