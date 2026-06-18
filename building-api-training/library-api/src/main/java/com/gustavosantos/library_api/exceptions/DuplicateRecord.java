package com.gustavosantos.library_api.exceptions;

public class DuplicateRecord extends RuntimeException {
    public DuplicateRecord(String message) {
        super(message);
    }
}
