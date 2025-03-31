package com.bookmanagmentapp.bookmanagmentapplication.exceptions;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    static final String ERROR_MESSAGE = "error";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        log.warn("⚠️ Ошибка валидации тела запроса: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString(); // Корректное имя параметра
            errors.put(fieldName, violation.getMessage());
        });

        log.warn("⚠️ Ошибка валидации параметров запроса: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBookNotFound(BookNotFoundException ex) {
        log.warn("⚠️ [BookNotFound] {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERROR_MESSAGE, ex.getMessage()));
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAuthorNotFound(AuthorNotFoundException ex) {
        log.warn("⚠️ [AuthorNotFound] {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERROR_MESSAGE, ex.getMessage()));
    }

    @ExceptionHandler(InvalidBookOperationException.class)
    public ResponseEntity<Map<String, String>> handleInvalidOperation(InvalidBookOperationException ex) {
        log.warn("⚠️ [InvalidOperation] {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(ERROR_MESSAGE, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        log.error("❌ Внутренняя ошибка сервера", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoResourceFound(NoResourceFoundException ex) {
        log.warn("⚠️ [404] Запрошенный ресурс не найден: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(ERROR_MESSAGE, "Запрашиваемый ресурс не найден"));
    }
}

