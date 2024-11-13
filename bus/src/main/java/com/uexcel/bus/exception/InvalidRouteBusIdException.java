package com.uexcel.bus.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Setter @Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRouteBusIdException extends RuntimeException {
    private List<String> invalidRouteOrBusIds;
    public InvalidRouteBusIdException(List<String> invalidRouteOrBusIds) {
        this.invalidRouteOrBusIds = invalidRouteOrBusIds;
    }
}
