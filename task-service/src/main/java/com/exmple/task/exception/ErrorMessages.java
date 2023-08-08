package com.exmple.task.exception;

import org.springframework.stereotype.Component;

@Component
public class ErrorMessages {
    public static final String MESSAGE_NOT_SENT = "Message was not sent";
    public static final String TASK_NOT_FOUND = "Task not found";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_IS_EXISTS = "User is exists";
}
