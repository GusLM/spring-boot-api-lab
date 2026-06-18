package com.gustavosantos.library_api.controller.dto.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

@Getter
public class StandardError {

    private int status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT")
    private Instant moment;

    private String path;

    private String message;

    private List<FieldError> fieldErrorList;

    public StandardError() {
    }

    public StandardError(int status, Instant moment, String path, String message, List<FieldError> fieldErrorList) {
        this.status = status;
        this.moment = moment;
        this.path = path;
        this.message = message;
        this.fieldErrorList = fieldErrorList;
    }

    public void addFieldError(FieldError fieldError) {
        fieldErrorList.add(fieldError);
    }

    public void removeFieldError(FieldError fieldError) {
        fieldErrorList.remove(fieldError);
    }

    public static StandardError defaultResponse(String message, HttpServletRequest request) {
        return new StandardError(
                HttpStatus.BAD_REQUEST.value(),
                Instant.now(),
                request.getRequestURI(),
                message,
                List.of()
        );
    }

    public static StandardError conflict(String message, HttpServletRequest request) {
        return new StandardError(
                HttpStatus.CONFLICT.value(),
                Instant.now(),
                request.getRequestURI(),
                message,
                List.of()
        );
    }
}
