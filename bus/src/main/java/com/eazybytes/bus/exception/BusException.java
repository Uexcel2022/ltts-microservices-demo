package com.eazybytes.bus.exception;

import com.eazybytes.bus.entity.Bus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
@Getter @Setter
@AllArgsConstructor
public class BusException extends RuntimeException {
    private List<Bus> buses;
}
