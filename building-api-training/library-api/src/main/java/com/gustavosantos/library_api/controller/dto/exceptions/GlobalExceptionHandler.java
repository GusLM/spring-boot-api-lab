package com.gustavosantos.library_api.controller.dto.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> methodArgumentNotValidException(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<FieldValidationError> list = fieldErrors
                .stream()
                .map(fe -> new FieldValidationError(fe.getField(), fe.getDefaultMessage())).toList();

        StandardError standardError = new StandardError(
                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                Instant.now(),
                request.getRequestURI(),
                "Validation Error",
                list
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(standardError);
    }
}
