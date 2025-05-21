package org.example.test_task.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class AdviceControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> exceptionMap = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .filter(error -> error.getDefaultMessage() != null)
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existingMsg, newMsg) -> existingMsg + "; " + newMsg // Обработка дубликатов
                ));
        log.warn(exceptionMap.toString());
        return ResponseEntity.badRequest().body(exceptionMap);
    }

    @ExceptionHandler(NotFountException.class)
    public ResponseEntity<Map<String, String>> handleNotFountExceptions(
            NotFountException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.notFound().build();
    }
}
