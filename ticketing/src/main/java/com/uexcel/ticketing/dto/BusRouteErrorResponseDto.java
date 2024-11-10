package com.uexcel.ticketing.dto;

import com.uexcel.ticketing.entity.BusRoute;
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
    private List<BusRoute> busRouteList;
    private String apiPath;
}
