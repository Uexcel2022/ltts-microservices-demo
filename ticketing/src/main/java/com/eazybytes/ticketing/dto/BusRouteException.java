package com.eazybytes.ticketing.dto;

import com.eazybytes.ticketing.entity.BusRoute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
@Getter @Setter
@AllArgsConstructor
public class BusRouteException extends RuntimeException {
    private List<BusRoute> busRoute;
}
