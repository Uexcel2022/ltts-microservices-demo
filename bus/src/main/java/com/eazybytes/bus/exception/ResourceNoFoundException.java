package com.eazybytes.bus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNoFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public ResourceNoFoundException(String resourceName,String fieldName, String fieldValue) {
        super(String.format(
                "%s is not found given the input data %s: %s",
                resourceName, fieldName, fieldValue )
        );
    }

}
