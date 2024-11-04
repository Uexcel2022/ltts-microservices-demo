package com.eazybytes.bus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidInputException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;
    public InvalidInputException(String message) {
        super(message);
    }
}
