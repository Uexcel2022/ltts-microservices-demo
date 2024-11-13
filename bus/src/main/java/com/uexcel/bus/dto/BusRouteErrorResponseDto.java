package com.uexcel.bus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;
@Getter @Setter
@AllArgsConstructor
public class BusRouteErrorResponseDto{
    private String timestamp;
    private int status;
    private HttpStatus error;
    private String message;
    private List<AddBusToRoute> addBusToRoute;
    private String apiPath;
}
