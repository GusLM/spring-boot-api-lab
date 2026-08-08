package com.gustavosantos.library_api.controller.common;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.exc.InvalidFormatException;
import com.gustavosantos.library_api.exceptions.DuplicateRecordException;
import com.gustavosantos.library_api.exceptions.ForbiddenOperationException;
import com.gustavosantos.library_api.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> httpMessageNotReadableException(
            HttpMessageNotReadableException e,
            HttpServletRequest request
    ) {

        Throwable cause = e.getCause();

        if (cause instanceof InvalidFormatException invalid &&
                UUID.class.equals(invalid.getTargetType())) {

            String field = invalid.getPath()
                    .stream()
                    .findFirst()
                    .map(JacksonException.Reference::getPropertyName)
                    .orElse("unknown");

            StandardError standardError = new StandardError(
                    HttpStatus.BAD_REQUEST.value(),
                    Instant.now(),
                    request.getRequestURI(),
                    "invalid UUID for field '" + field + "'",
                    List.of()
            );

            return ResponseEntity.status(standardError.getStatus()).body(standardError);
        }

        StandardError standardError = new StandardError(
                HttpStatus.BAD_REQUEST.value(),
                Instant.now(),
                request.getRequestURI(),
                "invalid request body",
                List.of()
        );

        return ResponseEntity.status(standardError.getStatus()).body(standardError);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardError> methodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e,
            HttpServletRequest request
    ) {

        StandardError standardError;

        if (UUID.class.equals(e.getRequiredType())) {

            standardError = new StandardError(
                    HttpStatus.BAD_REQUEST.value(),
                    Instant.now(),
                    request.getRequestURI(),
                    "invalid UUID for field '" + e.getName() + "'",
                    List.of()
            );

            return ResponseEntity.status(standardError.getStatus()).body(standardError);
        }

        standardError = new StandardError(
                HttpStatus.BAD_REQUEST.value(),
                Instant.now(),
                request.getRequestURI(),
                "invalid parameter",
                List.of()
        );

        return ResponseEntity.status(standardError.getStatus()).body(standardError);
    }

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
