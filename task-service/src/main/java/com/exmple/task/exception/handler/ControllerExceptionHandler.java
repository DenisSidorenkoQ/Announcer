package com.exmple.task.exception.handler;

import com.exmple.task.dto.response.ErrorResponse;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler({EntityNotFoundException.class})
  @ResponseStatus(value = NOT_FOUND)
  public ErrorResponse handleEntityNotFoundException(
      final EntityNotFoundException ex, final WebRequest request) {
    return new ErrorResponse(NOT_FOUND.value(), ex.getMessage(), request.getDescription(false));
  }

  @ExceptionHandler
  @ResponseStatus(value = CONFLICT)
  public ErrorResponse handleEntityExistsException(
          final EntityExistsException ex, final WebRequest request) {
    return new ErrorResponse(CONFLICT.value(), ex.getMessage(), request.getDescription(false));
  }

  @ExceptionHandler
  @ResponseStatus(value = INTERNAL_SERVER_ERROR)
  public ErrorResponse handleGlobalException(final Exception ex, final WebRequest request) {
    return new ErrorResponse(
            INTERNAL_SERVER_ERROR.value(), ex.getMessage(), request.getDescription(false));
  }
}
