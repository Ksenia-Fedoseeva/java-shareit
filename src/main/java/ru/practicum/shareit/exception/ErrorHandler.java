package ru.practicum.shareit.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Error> handleValidationException(Exception e) {
        log.error("Ошибка", e);
        Error error = new Error(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Error> handleEmailAlreadyExists(EmailAlreadyExistsException e) {
        Error error = new Error(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Error> handleNotFoundException(NotFoundException e) {
        log.error("Ошибка", e);
        Error error = new Error(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Error> handleAccessDeniedException(AccessDeniedException e) {
        log.error("Ошибка", e);
        Error error = new Error(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}