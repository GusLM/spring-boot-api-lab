package com.gustavosantos.library_api.controller.dto.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

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

    public StandardError(int status, Instant moment, String path, String message) {
        this.status = status;
        this.moment = moment;
        this.path = path;
        this.message = message;
    }

    public void addFieldError(FieldError fieldError) {
        fieldErrorList.add(fieldError);
    }

    public void removeFieldError(FieldError fieldError) {
        fieldErrorList.remove(fieldError);
    }
}
