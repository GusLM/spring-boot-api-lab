package com.gustavosantos.library_api.controller.dto.exceptions;

import com.gustavosantos.library_api.exceptions.DuplicateRecordException;
import com.gustavosantos.library_api.exceptions.ForbiddenOperationException;
import com.gustavosantos.library_api.exceptions.ResourceNotFoundException;
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

    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity<StandardError> duplicateRecordException(
            DuplicateRecordException e,
            HttpServletRequest request
    ) {
        StandardError standardError = new StandardError(
                HttpStatus.CONFLICT.value(),
                Instant.now(),
                request.getRequestURI(),
                e.getMessage(),
                List.of()
        );

        return ResponseEntity.status(standardError.getStatus()).body(standardError);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFoundException(
            ResourceNotFoundException e,
            HttpServletRequest request
    ) {
        StandardError standardError = new StandardError(
                HttpStatus.NOT_FOUND.value(),
                Instant.now(),
                request.getRequestURI(),
                e.getMessage(),
                List.of()
        );

        return ResponseEntity.status(standardError.getStatus()).body(standardError);
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<StandardError> forbiddenOperationException(
            ForbiddenOperationException e,
            HttpServletRequest request
    ) {
        StandardError standardError = new StandardError(
                HttpStatus.FORBIDDEN.value(),
                Instant.now(),
                request.getRequestURI(),
                e.getMessage(),
                List.of()
        );

        return ResponseEntity.status(standardError.getStatus()).body(standardError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> illegalArgumentException(
            IllegalArgumentException e,
            HttpServletRequest request
    ) {
        StandardError standardError = new StandardError(
                HttpStatus.BAD_REQUEST.value(),
                Instant.now(),
                request.getRequestURI(),
                e.getMessage(),
                List.of()
        );

        return ResponseEntity.status(standardError.getStatus()).body(standardError);
    }
}
