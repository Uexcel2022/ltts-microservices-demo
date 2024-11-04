package com.eazybytes.bus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class ExceptionFail extends RuntimeException {
    public ExceptionFail(String message){
        super(message);
    }
}
