package com.eazybytes.ticketing.exception;

import com.eazybytes.ticketing.entity.Route;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
@Getter @Setter
@AllArgsConstructor
public class RouteException extends RuntimeException {
    private List<Route> routes;
}
